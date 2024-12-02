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
import vn.com.atomi.loyalty.core.dto.output.GetListPackageOutput;
import vn.com.atomi.loyalty.core.entity.CChainMission;
import vn.com.atomi.loyalty.core.entity.Customer;
import vn.com.atomi.loyalty.core.entity.Packages;
import vn.com.atomi.loyalty.core.entity.TransExternal;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.RefType;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.repository.*;
import vn.com.atomi.loyalty.core.service.NotificationService;
import vn.com.atomi.loyalty.core.service.PackageService;
import vn.com.atomi.loyalty.core.utils.Constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
    TransExternal packageResponse = transExternalRepository.
            findTransExternalByCondition(
                    purchasePackageInput.getCifNo(),
                    purchasePackageInput.getPackageId(),
                    RefType.PACKAGE);
    if(packageResponse != null) {
      throw new BaseException(ErrorCode.CUSTOMER_REGISTED_PACKAGE);
    }
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
    return super.modelMapper.convertRegistedPackageOutput(packageResponse);
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
        StringBuilder notiContent = new StringBuilder(Constants.Notification.MISSION_CONTENT + " ");
        if(packages.isPresent()) {
            notiContent.append(packages.get().getName());
        }
        notificationService.sendNotification(
                Constants.Notification.PACKAGE_TITLE,
                notiContent.toString(),
                customer.getPhone());
    }
}
