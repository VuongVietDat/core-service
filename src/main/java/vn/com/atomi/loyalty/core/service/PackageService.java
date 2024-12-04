package vn.com.atomi.loyalty.core.service;

import vn.com.atomi.loyalty.core.dto.input.PurchasePackageInput;
import vn.com.atomi.loyalty.core.dto.output.GetListBenefitOutput;
import vn.com.atomi.loyalty.core.dto.output.GetListCustomerBenefitOutput;
import vn.com.atomi.loyalty.core.dto.output.GetListPackageOutput;
import vn.com.atomi.loyalty.core.dto.output.RegistedPackageOuput;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
public interface PackageService {
  List<GetListPackageOutput> getListPackage();
  List<GetListBenefitOutput> getListBenefit(Long packageId);
  String purchasePackage(PurchasePackageInput purchasePackageInput);
  GetListPackageOutput getRegistedPackage(String cifNo);
  List<GetListCustomerBenefitOutput> getListCustomerBenefit(Long packageId, String cifNo);
}
