package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.core.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.core.dto.input.CustomerGroupInput;
import vn.com.atomi.loyalty.core.dto.input.PurchasePackageInput;
import vn.com.atomi.loyalty.core.dto.output.*;
import vn.com.atomi.loyalty.core.entity.PkgPurchaseHistory;
import vn.com.atomi.loyalty.core.enums.ApprovalStatus;
import vn.com.atomi.loyalty.core.enums.ApprovalType;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.feign.LoyaltyConfigClient;
import vn.com.atomi.loyalty.core.repository.*;
import vn.com.atomi.loyalty.core.service.PackageService;
import vn.com.atomi.loyalty.core.utils.Constants;
import vn.com.atomi.loyalty.core.utils.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class PackageServiceImpl extends BaseService implements PackageService {

  private final PackageRepository packageRepository;

  private final PkgBenefitRepository benefitRepository;

  private final PkgPurchaseHistoryRepository purchaseHistoryRepository;

  private final CustomerRepository customerRepository;

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
  public void purchasePackage(PurchasePackageInput purchasePackageInput) {
    purchaseHistoryRepository.save(this.mappingPurchasePackage(purchasePackageInput));
  }
  @Override
  public RegistedPackageOuput getRegistedPackage(String cifNo) {
    var packageResponse = purchaseHistoryRepository.getRegistedPackage(cifNo);
    return super.modelMapper.convertRegistedPackageOutput(packageResponse);
  }

  private PkgPurchaseHistory mappingPurchasePackage(PurchasePackageInput purchasePackageInput){
    PkgPurchaseHistory pkgPurchaseHistory = new PkgPurchaseHistory();
    try {
      var customer = customerRepository.findByCifBank(purchasePackageInput.getCifNo());
      customer.ifPresent(value -> purchasePackageInput.setCustomerId(value.getId()));

      pkgPurchaseHistory.setCustomerId( purchasePackageInput.getCustomerId() );
      pkgPurchaseHistory.setCifNo( purchasePackageInput.getCifNo() );
      pkgPurchaseHistory.setPackageId( purchasePackageInput.getPackageId() );
      pkgPurchaseHistory.setRefNo( purchasePackageInput.getRefNo() );
      pkgPurchaseHistory.setTransId( purchasePackageInput.getTransId() );
      if (StringUtils.isNotBlank(purchasePackageInput.getPurchasedDate())) {
        pkgPurchaseHistory.setPurchasedDate(LocalDate.parse(purchasePackageInput.getPurchasedDate()
                , DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)));
      }
      if (StringUtils.isNotBlank(purchasePackageInput.getEffectiveDate())) {
        pkgPurchaseHistory.setEffectiveDate(LocalDate.parse(purchasePackageInput.getEffectiveDate()
                ,DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
      }
      if (StringUtils.isNotBlank(purchasePackageInput.getExpiredDate())) {
        pkgPurchaseHistory.setExpiredDate(LocalDate.parse(purchasePackageInput.getExpiredDate()
                ,DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
      }
      pkgPurchaseHistory.setTxnAmount( purchasePackageInput.getTxnAmount() );
      pkgPurchaseHistory.setTxnStatus( purchasePackageInput.getTxnStatus() );
      pkgPurchaseHistory.setTxnCurrency( purchasePackageInput.getTxnCurrency() );
      pkgPurchaseHistory.setPaymentMethod( purchasePackageInput.getPaymentMethod() );
      pkgPurchaseHistory.setPaymentChannel( purchasePackageInput.getPaymentChannel() );
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return pkgPurchaseHistory;
  }
}
