package vn.com.atomi.loyalty.core.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.core.dto.input.PurchasePackageInput;
import vn.com.atomi.loyalty.core.dto.output.GetListBenefitOutput;
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
  ResponsePage<GetListPackageOutput> getPagePackage(Pageable pageable);

  ResponsePage<GetListBenefitOutput>  getPageBenefit(Long packageId, Pageable pageable);
}
