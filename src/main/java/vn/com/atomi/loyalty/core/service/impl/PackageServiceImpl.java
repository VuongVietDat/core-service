package vn.com.atomi.loyalty.core.service.impl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.core.dto.input.PurchaseChainMissionInput;
import vn.com.atomi.loyalty.core.dto.input.PurchasePackageInput;
import vn.com.atomi.loyalty.core.dto.output.*;
import vn.com.atomi.loyalty.core.entity.*;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.RefType;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.repository.*;
import vn.com.atomi.loyalty.core.service.NotificationService;
import vn.com.atomi.loyalty.core.service.PackageService;
import vn.com.atomi.loyalty.core.utils.Constants;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    private final PkgGiftMappingRepository giftMappingRepository;

    @Override
    public List<GetListPackageOutput> getListPackage() {
    var listPackagePage = packageRepository.getListPackage(Status.ACTIVE);
    return super.modelMapper.convertPackageOutput(listPackagePage);
    }
    @Override
    public List<GetListBenefitOutput> getListBenefit(Long packageId) {
        var listPackagePage = giftMappingRepository.getListGiftBenefit(packageId, Status.ACTIVE.name());
        if(listPackagePage != null) {
            return mappingListGiftBenefit(listPackagePage);
        }
        return null;
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

    List<PkgCustomerBenefitOutput> convertCustomerBenefit(List<Object[]> benefits) {
        return benefits.stream()
                .map(data -> {
                    PkgCustomerBenefitOutput output = new PkgCustomerBenefitOutput();

                    output.setId(data.length > 0 ? Long.valueOf((String) data[0]) : null);
                    output.setPackageId(data.length > 1 ? Long.valueOf((String) data[1]) : null);
                    output.setCustomerId(data.length > 2 ? Long.valueOf((String) data[2]) : null);
                    output.setGiftPartnerId(data.length > 3 ? Long.valueOf((String) data[3]) : null);
                    output.setType(data.length > 4 ? String.valueOf((Character) data[4]) : null);
                    output.setCode(data.length > 5 ? (String) data[5] : null);
                    output.setName(data.length > 6 ? (String) data[6] : null);
                    output.setStatus(data.length > 7 ? (String) data[7] : null);
                    output.setQuantity(data.length > 8 ? (Integer) data[8] : null);
                    output.setUrlImage(data.length > 9 ? (String) data[9] : null);
                    output.setDescription(data.length > 10 ? (String) data[10] : null);
                    output.setStartDate(data.length > 11 ? (String) data[11] : null);
                    output.setEndDate(data.length > 12 ? (String) data[12] : null);
                    return output;

                }).collect(Collectors.toList());
    }
    private void handleCloneDataCustomerBenefit (PurchasePackageInput purchasePackageInput, Customer customer) {
        var benefitList = giftMappingRepository.getListPkgBenefitMapping(purchasePackageInput.getPackageId(), Status.ACTIVE, Constants.isDeleted.NO);
        customerBenefitRepository.saveAll(mappingDataCreateCustomerBenefit(purchasePackageInput, benefitList, customer));
    }
    private List<PkgCustomerBenefit> mappingDataCreateCustomerBenefit(PurchasePackageInput purchasePackageInput, List<PkgGiftMapping> benefit, Customer customer){
        return benefit.stream()
                .map(data -> {
                    PkgCustomerBenefit output = new PkgCustomerBenefit();
                    output.setCustomerId(customer.getId());
                    output.setPackageId(data.getPackageId());
                    output.setGiftPartnerId(data.getGiftPartnerId());
                    output.setCifNo(customer.getCifBank());
                    output.setStatus(Constants.Packages.STATUS_AVAILABLE);
                    output.setGiftQuantity(1);
                    output.setDisplayOrder(null);
                    if(StringUtils.isNotBlank(purchasePackageInput.getPurchasedDate())) {
                        output.setTxnDate(LocalDate.parse(purchasePackageInput.getPurchasedDate(),
                                DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)));
                    }
                    if(StringUtils.isNotBlank(purchasePackageInput.getEffectiveDate())) {
                        output.setEffectiveDate(LocalDate.parse(purchasePackageInput.getEffectiveDate(),
                                DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
                    }
                    if(StringUtils.isNotBlank(purchasePackageInput.getExpiredDate())) {
                        output.setExpireDate(LocalDate.parse(purchasePackageInput.getExpiredDate(),
                                DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
                    }
                    return output;

                }).collect(Collectors.toList());

    }
    private List<GetListBenefitOutput> mappingListGiftBenefit (List<Object[]> rawData){
        return rawData.stream()
                .map(data -> {
                    GetListBenefitOutput output = new GetListBenefitOutput();

                    output.setId(data.length > 0 ? (Long) data[0] : null);
                    output.setPackageId(data.length > 1 ? Long.valueOf((Integer) data[1]) : null);
                    output.setType(data.length > 2 ? String.valueOf((Character) data[2]) : null);
                    output.setCode(data.length > 3 ? (String) data[3] : null);
                    output.setName(data.length > 4 ? (String) data[4] : null);
                    output.setQuantity(data.length > 5 ? (Integer) data[5] : null);
                    output.setUrlImage(data.length > 6 ? (String) data[6] : null);
                    output.setDescription(data.length > 7 ? (String) data[7] : null);

                    return output;

                }).collect(Collectors.toList());
    }

}
