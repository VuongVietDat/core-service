package vn.com.atomi.loyalty.core.service.impl;

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
    var listPackagePage = giftMappingRepository.getListGiftBenefits(packageId, Status.ACTIVE.name());
//    return super.modelMapper.convertBenefitOutput(listPackagePage);
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
//        this.handleCloneDataCustomerBenefit(purchasePackageInput, customer.get());

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
//        return this.convertCustomerBenefit(customerBenefit);
        return null;
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

//    List<PkgCustomerBenefitOutput> convertCustomerBenefit(List<PkgCustomerBenefit> benefits) {
//        return benefits.stream()
//                .map(data -> {
//                    PkgCustomerBenefitOutput output = new PkgCustomerBenefitOutput();
//
//                    output.setId(data.getId());
//                    output.setPackageId(data.getPackageId());
//                    output.setCustomerId(data.getCustomerId());
//                    output.setGiftPartnerId(data.getGiftPartnerId());
//                    output.setName(data.getName());
//                    output.setStatus(data.getStatus());
//                    output.setType(data.getType());
//                    output.setQuantity(data.getQuantity());
//                    if(data.getStartDate() != null) {
//                        output.setStartDate(data.getStartDate().format(DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
//                    }
//                    if (data.getEndDate() != null) {
//                        output.setEndDate(data.getEndDate().format(DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
//                    }
//                    output.setDisplayOrder(data.getDisplayOrder());
//                    output.setUrlImage(data.getUrlImage());
//                    output.setDescription(data.getDescription());
//                    return output;
//
//                }).collect(Collectors.toList());
//    }
//    private void handleCloneDataCustomerBenefit (PurchasePackageInput purchasePackageInput, Customer customer) {
//        var benefitList = benefitRepository.getListBenefit(purchasePackageInput.getPackageId(), Status.ACTIVE);
//        customerBenefitRepository.saveAll(mappingDataCreateCustomerBenefit(benefitList, customer));
//    }
//    private List<PkgCustomerBenefit> mappingDataCreateCustomerBenefit(List<PkgBenefit> benefits, Customer customer){
//        return benefits.stream()
//                .map(data -> {
//                    PkgCustomerBenefit output = new PkgCustomerBenefit();
//                    output.setCustomerId(customer.getId());
//                    output.setPackageId(data.getPackageId());
//                    output.setGiftPartnerId(data.getGiftPartnerId());
//                    output.setCifNo(customer.getCifBank());
//                    output.setName(data.getName());
//                    output.setStatus(Constants.Packages.STATUS_AVAILABLE);
//                    output.setType(data.getType());
//                    output.setQuantity(1);
//                    if(data.getStartDate() != null) {
//                        output.setStartDate(data.getStartDate().toLocalDate());
//                    }
//                    if(data.getEndDate() != null) {
//                        output.setEndDate(data.getEndDate().toLocalDate());
//                    }
//                    output.setDisplayOrder(data.getDisplayOrder());
//                    output.setUrlImage(data.getUrlImage());
//                    output.setDescription(data.getDescription());
//                    output.setCreatedAt(LocalDate.now());
//                    output.setCreatedBy(1L);
//                    return output;
//
//                }).collect(Collectors.toList());
//
//    }
//private List<GetListBenefitOutput> mappingListGiftBenefit (List<Object[]> rawData){
//    return rawData.stream()
//            .map(data -> {
//                GetListBenefitOutput output = new GetListBenefitOutput();
//
//                output.setChainId(data.length > 0 ? Long.valueOf((String) data[0]) : null);  // Assuming column 0 is chainId
//                output.setId(data.length > 1 ? Long.valueOf((String) data[1]) : null);  // Assuming column 1 is missionId
//                output.setOrderNo(data.length > 2 ? Integer.parseInt((String) data[2]) : null);  // Assuming column 2 is orderNo
//                output.setGroupType(data.length > 3 ? (String) data[3] : null);  // Assuming column 3 is groupType
//                output.setCode(data.length > 4 ? (String) data[4] : null);  // Assuming column 4 is code
//                output.setName(data.length > 5 ? (String) data[5] : null);  // Assuming column 5 is name
//                output.setBenefitType(data.length > 6 ? (String) data[6] : null);  // Assuming column 6 is benefitType
//                if (data.length > 7 && data[7] != null) {
//                    Timestamp startDate = (Timestamp) data[7];
//                    output.setStartDate(new SimpleDateFormat(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE).format(startDate.getTime()));
//                }
//                if (data.length > 8 && data[8] != null) {
//                    Timestamp endDate = (Timestamp) data[8];
//                    output.setEndDate(new SimpleDateFormat(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE).format(endDate.getTime()));
//                }
//                output.setPrice(data.length == 9? new BigDecimal((String) data[9]) : null);  // Assuming column 9 is price
//                output.setCurrency(data.length > 10 ? (String) data[10] : null);  // Assuming column 10 is currency
//                output.setImage(data.length > 11 ? (String) data[11] : null);  // Assuming column 11 is image
//                output.setNotes(data.length > 12 ? (String) data[12] : null);  // Assuming column 12 is notes
//                output.setStatus(data.length > 13 ? (String) data[13] : null);  // Assuming column 13 is notes
//                return output;
//
//            }).collect(Collectors.toList());
//}

}
