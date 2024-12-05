package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.core.dto.input.PurchaseChainMissionInput;
import vn.com.atomi.loyalty.core.dto.input.PurchasePackageInput;
import vn.com.atomi.loyalty.core.dto.output.GetListBenefitOutput;
import vn.com.atomi.loyalty.core.dto.output.GetListCustomerBenefitOutput;
import vn.com.atomi.loyalty.core.dto.output.GetListPackageOutput;
import vn.com.atomi.loyalty.core.dto.output.PkgCustomerBenefitOutput;
import vn.com.atomi.loyalty.core.entity.*;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.RefType;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.repository.*;
import vn.com.atomi.loyalty.core.service.NotificationService;
import vn.com.atomi.loyalty.core.service.PackageService;
import vn.com.atomi.loyalty.core.utils.Constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class PackageServiceImpl extends BaseService implements PackageService {

    private final PackageRepository packageRepository;

    private final PkgBenefitRepository benefitRepository;

    private final TransExternalRepository transExternalRepository;

    private final CustomerRepository customerRepository;

    private final NotificationService notificationService;

    private final PkgCustomerBenefitRepository customerBenefitRepository;

    @Override
    public List<GetListPackageOutput> getListPackage() {
    var listPackagePage = packageRepository.getListPackage(Status.ACTIVE);
    return super.modelMapper.convertPackageOutput(listPackagePage);
    }
    @Override
    public List<GetListBenefitOutput> getListBenefit(Long packageId) {
    var listPackagePage = benefitRepository.getListBenefit(packageId, Status.ACTIVE);
    return super.modelMapper.convertBenefitOutput(listPackagePage);
    }
    @Override
    public String purchasePackage(PurchasePackageInput purchasePackageInput) {
        // get customer by cif
        Optional<Customer> customer = customerRepository.
                findByCifBank(purchasePackageInput.getCifNo());
        if(!customer.isPresent()) {
          throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
        }
        // check package by customer id case registed package before
        Integer packageResponse = transExternalRepository.
            findTransExternalByCondition(
                    purchasePackageInput.getCifNo(),
                    purchasePackageInput.getPackageId(),
                    RefType.PACKAGE);
        if(packageResponse != null) {
          throw new BaseException(ErrorCode.CUSTOMER_REGISTED_PACKAGE);
        }
        // handle data save to pkg_customer_benefit
        this.handleCloneDataCustomerBenefit(purchasePackageInput, customer.get());

        // insert log to trans_external
        TransExternal history = mappingPurchasePackage(purchasePackageInput, customer.get());
        transExternalRepository.save(history);
        if (history.getId() != null) {
            // call push notification to app LPB
          CompletableFuture.runAsync(() -> {
              try {
                  this.handleNotification(customer.get(), purchasePackageInput);
              } catch (Exception e) {
                  e.printStackTrace();
              }
              System.out.println("Push notification app LPB - Transaction Id: " + history.getId() + " already done!");
          });
        }
        return history.getId();
    }
    @Override
    public GetListPackageOutput getRegistedPackage(String cifNo) {
    var packageResponse = packageRepository.getRegistedPackage(Status.ACTIVE, cifNo, RefType.PACKAGE);
        if(packageResponse == null) {
            return new GetListPackageOutput();
        }
    return super.modelMapper.convertRegistedPackageOutput(packageResponse);
    }
    @Override
    public List<PkgCustomerBenefitOutput> getListCustomerBenefit(Long packageId, String cifNo,String status) {
        List<String> lstStatus = new ArrayList<>();
        if(Constants.Packages.STATUS_AVAILABLE.equalsIgnoreCase(status)) {
            lstStatus.add(status);
        } else {
            lstStatus.add(Constants.Packages.STATUS_USED);
            lstStatus.add(Constants.Packages.STATUS_EXPIRE);
        }
        var customerBenefit = customerBenefitRepository.findByCondition(packageId, cifNo, lstStatus);
        if(customerBenefit.isEmpty()) {
            return new ArrayList<>();
        }
        return this.convertCustomerBenefit(customerBenefit);
    }

    private TransExternal mappingPurchasePackage(PurchasePackageInput purchasePackageInput, Customer customer){
      TransExternal response = new TransExternal();
    try {
        response.setId(UUID.randomUUID().toString());
        response.setCustomerId( customer.getId() );
        response.setPhoneNo( customer.getPhone() );
        response.setCifNo( purchasePackageInput.getCifNo() );
        response.setRefId( purchasePackageInput.getPackageId() );
        response.setRefType( RefType.PACKAGE );
        response.setTxnRefNo( purchasePackageInput.getRefNo() );
        response.setTxnId( purchasePackageInput.getTransId() );
        if (StringUtils.isNotBlank(purchasePackageInput.getPurchasedDate())) {
            response.setTxnDate(LocalDate.parse(purchasePackageInput.getPurchasedDate()
                  , DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)));
        }
        if (StringUtils.isNotBlank(purchasePackageInput.getEffectiveDate())) {
            response.setEffectiveDate(LocalDate.parse(purchasePackageInput.getEffectiveDate()
                  ,DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
        }
        if (StringUtils.isNotBlank(purchasePackageInput.getExpiredDate())) {
            response.setExpiredDate(LocalDate.parse(purchasePackageInput.getExpiredDate()
                  ,DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
        }
        response.setTxnAmount( purchasePackageInput.getTxnAmount() );
        response.setTxnStatus(Constants.Status.SUCCESS);
        response.setTxnCurrency( purchasePackageInput.getTxnCurrency() );
        response.setTxnMethod( purchasePackageInput.getPaymentMethod() );
        response.setTxnChannel( purchasePackageInput.getPaymentChannel() );
        response.setTxnNote( purchasePackageInput.getNotes() );
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return response;
    }

    public void handleNotification(Customer customer,
                                   PurchasePackageInput purchasePackageInput) {
        Optional<Packages> packages = packageRepository.findById(purchasePackageInput.getPackageId());
        StringBuilder notiContent = new StringBuilder(Constants.Notification.PACKAGE_CONTENT + " ");
        if(packages.isPresent()) {
            notiContent.append(packages.get().getName());
        }
        notificationService.sendNotification(
                Constants.Notification.PACKAGE_TITLE,
                notiContent.toString(),
                customer.getPhone());
    }

    List<PkgCustomerBenefitOutput> convertCustomerBenefit(List<PkgCustomerBenefit> benefits) {
        return benefits.stream()
                .map(data -> {
                    PkgCustomerBenefitOutput output = new PkgCustomerBenefitOutput();

                    output.setId(data.getId());
                    output.setPackageId(data.getPackageId());
                    output.setCustomerId(data.getCustomerId());
                    output.setGiftPartnerId(data.getGiftPartnerId());
                    output.setName(data.getName());
                    output.setStatus(data.getStatus());
                    output.setType(data.getType());
                    output.setQuantity(data.getQuantity());
                    if(data.getStartDate() != null) {
                        output.setStartDate(data.getStartDate().format(DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
                    }
                    if (data.getEndDate() != null) {
                        output.setEndDate(data.getEndDate().format(DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
                    }
                    output.setDisplayOrder(data.getDisplayOrder());
                    output.setUrlImage(data.getUrlImage());
                    output.setDescription(data.getDescription());
                    return output;

                }).collect(Collectors.toList());
    }
    private void handleCloneDataCustomerBenefit (PurchasePackageInput purchasePackageInput, Customer customer) {
        var benefitList = benefitRepository.getListBenefit(purchasePackageInput.getPackageId(), Status.ACTIVE);
        customerBenefitRepository.saveAll(mappingDataCreateCustomerBenefit(benefitList, customer));
    }
    private List<PkgCustomerBenefit> mappingDataCreateCustomerBenefit(List<PkgBenefit> benefits, Customer customer){
        return benefits.stream()
                .map(data -> {
                    PkgCustomerBenefit output = new PkgCustomerBenefit();
                    output.setCustomerId(customer.getId());
                    output.setPackageId(data.getPackageId());
                    output.setGiftPartnerId(data.getGiftPartnerId());
                    output.setCifNo(customer.getCifBank());
                    output.setName(data.getName());
                    output.setStatus(Constants.Packages.STATUS_AVAILABLE);
                    output.setType(data.getType());
                    output.setQuantity(1);
                    if(data.getStartDate() != null) {
                        output.setStartDate(data.getStartDate().toLocalDate());
                    }
                    if(data.getEndDate() != null) {
                        output.setEndDate(data.getEndDate().toLocalDate());
                    }
                    output.setDisplayOrder(data.getDisplayOrder());
                    output.setUrlImage(data.getUrlImage());
                    output.setDescription(data.getDescription());
                    output.setCreatedAt(LocalDate.now());
                    output.setCreatedBy(1L);
                    return output;

                }).collect(Collectors.toList());

    }

}
