package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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

import java.time.LocalDateTime;
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

  @Override
  public List<GetListPackageOutput> getListPackage() {
    var listPackagePage = packageRepository.getListPackage(Status.ACTIVE);
    return super.modelMapper.convertPackageOutput(listPackagePage);
  }
  @Override
  public List<GetListBenefitOutput> getListBenefit(Integer packageId) {
    var listPackagePage = benefitRepository.getListBenefit(packageId, Status.ACTIVE);
    return super.modelMapper.convertBenefitOutput(listPackagePage);
  }
  @Override
  public void purchasePackage(PurchasePackageInput purchasePackageInput) {
    purchaseHistoryRepository.save(super.modelMapper.convertPerchaseHistoryInput(purchasePackageInput));
  }
  @Override
  public GetListPackageOutput getRegistedPackage(String cifNo) {
    var packageResponse = packageRepository.getReferenceById(null);
    return super.modelMapper.convertRegistedPackageOutput(packageResponse);
  }
}
