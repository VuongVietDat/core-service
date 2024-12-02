package vn.com.atomi.loyalty.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.core.dto.input.PurchasePackageInput;
import vn.com.atomi.loyalty.core.dto.output.GetListBenefitOutput;
import vn.com.atomi.loyalty.core.dto.output.GetListPackageOutput;
import vn.com.atomi.loyalty.core.entity.Customer;
import vn.com.atomi.loyalty.core.entity.TransExternal;
import vn.com.atomi.loyalty.core.enums.ErrorCode;
import vn.com.atomi.loyalty.core.enums.RefType;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.repository.*;
import vn.com.atomi.loyalty.core.service.PackageService;
import vn.com.atomi.loyalty.core.utils.Constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public List<GetListPackageOutput> getListPackage() {
        var listPackagePage = packageRepository.getListPackage(Status.ACTIVE);
        return super.modelMapper.convertPackageOutput(listPackagePage);
    }

    @Override
    public ResponsePage<GetListPackageOutput> getPagePackage(Pageable pageable) {
        var  packagePage = packageRepository.getPagePackage(Status.ACTIVE, pageable);
        return new ResponsePage<>(packagePage, new ArrayList<>());
    }

    @Override
    public ResponsePage<GetListBenefitOutput> getPageBenefit(Long packageId, Pageable pageable) {
        var pagePackagePage = benefitRepository.getPageBenefit(packageId, Status.ACTIVE, pageable);
        return new ResponsePage<>(pagePackagePage, new ArrayList<>());
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
        if (!customer.isPresent()) {
            throw new BaseException(ErrorCode.CUSTOMER_NOT_EXISTED);
        }
        // check package by customer id case registed package before
        TransExternal packageResponse = transExternalRepository.
                findTransExternalByCondition(
                        purchasePackageInput.getCifNo(),
                        purchasePackageInput.getPackageId(),
                        RefType.PACKAGE);
        if (packageResponse != null) {
            throw new BaseException(ErrorCode.CUSTOMER_REGISTED_PACKAGE);
        }
        TransExternal history = mappingPurchasePackage(purchasePackageInput, customer.get());
        transExternalRepository.save(history);
        return history.getId();
    }

    @Override
    public GetListPackageOutput getRegistedPackage(String cifNo) {
        var packageResponse = packageRepository.getRegistedPackage(Status.ACTIVE, cifNo, RefType.PACKAGE);
        return super.modelMapper.convertRegistedPackageOutput(packageResponse);
    }


    private TransExternal mappingPurchasePackage(PurchasePackageInput purchasePackageInput, Customer customer) {
        TransExternal response = new TransExternal();
        try {
            response.setId(UUID.randomUUID().toString());
            response.setCustomer(customer.getId());
            response.setPhoneNo(customer.getPhone());
            response.setCifNo(purchasePackageInput.getCifNo());
            response.setRefId(purchasePackageInput.getPackageId());
            response.setRefType(RefType.PACKAGE);
            response.setTxnRefNo(purchasePackageInput.getRefNo());
            response.setTxnId(purchasePackageInput.getTransId());
            if (StringUtils.isNotBlank(purchasePackageInput.getPurchasedDate())) {
                response.setTxnDate(LocalDate.parse(purchasePackageInput.getPurchasedDate()
                        , DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS_STROKE)));
            }
            if (StringUtils.isNotBlank(purchasePackageInput.getEffectiveDate())) {
                response.setEffectiveDate(LocalDate.parse(purchasePackageInput.getEffectiveDate()
                        , DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
            }
            if (StringUtils.isNotBlank(purchasePackageInput.getExpiredDate())) {
                response.setExpiredDate(LocalDate.parse(purchasePackageInput.getExpiredDate()
                        , DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)));
            }
            response.setTxnAmount(purchasePackageInput.getTxnAmount());
            response.setTxnStatus(Constants.Status.SUCCESS);
            response.setTxnCurrency(purchasePackageInput.getTxnCurrency());
            response.setTxnMethod(purchasePackageInput.getPaymentMethod());
            response.setTxnChannel(purchasePackageInput.getPaymentChannel());
            response.setTxnNote(purchasePackageInput.getNotes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }
}
