package vn.com.atomi.loyalty.core.service;

import vn.com.atomi.loyalty.core.dto.input.PurchasePackageInput;
import vn.com.atomi.loyalty.core.dto.output.*;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
public interface PackageService {
  List<GetListPackageOutput> getListPackage();
  List<GetListBenefitOutput> getListBenefit(Long packageId);
  void purchasePackage(PurchasePackageInput purchasePackageInput);
  RegistedPackageOuput getRegistedPackage(String cifNo);
}
