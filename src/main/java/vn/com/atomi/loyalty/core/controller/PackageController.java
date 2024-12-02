package vn.com.atomi.loyalty.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.core.dto.input.PurchasePackageInput;
import vn.com.atomi.loyalty.core.dto.output.*;
import vn.com.atomi.loyalty.core.service.PackageService;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class PackageController extends BaseController {

    private final PackageService packageService;

    @Operation(summary = "Api danh sách gói hội viên")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/package/list-package")
    public ResponseEntity<ResponseData<List<GetListPackageOutput>>> getListPackage(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey) {
        List<GetListPackageOutput> lstResponse = packageService.getListPackage();
        return ResponseUtils.success(lstResponse);
    }

    @Operation(summary = "Api danh sách gói hội viên - Phân trang")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/package/page-package")
    public ResponseEntity<ResponseData<ResponsePage<GetListPackageOutput>>> getPagePackage(
            @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
            Integer pageNo,
            @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10")
            @RequestParam
            Integer pageSize,
            @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
            @RequestParam(required = false)
            String sort,
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey) {
        ResponsePage<GetListPackageOutput> pageResponse = packageService.getPagePackage(super.pageable(pageNo, pageSize, sort));
        return ResponseUtils.success(pageResponse);
    }


    @Operation(summary = "Api danh sách ưu đãi theo gói hội viên")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/package/list-benefit")
    public ResponseEntity<ResponseData<List<GetListBenefitOutput>>> getListBenefit(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "Mã định danh gói hội viên")
            @RequestParam(required = false)
            Long packageId) {
        List<GetListBenefitOutput> lstResponse = packageService.getListBenefit(packageId);
        return ResponseUtils.success(lstResponse);
    }

    @Operation(summary = "Api danh sách ưu đãi theo gói hội viên - Phân trang")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/package/page-benefit")
    public ResponseEntity<ResponseData<ResponsePage<GetListBenefitOutput>>> getPageBenefit(
            @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
            Integer pageNo,
            @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10")
            @RequestParam
            Integer pageSize,
            @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
            @RequestParam(required = false)
            String sort,
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "Mã định danh gói hội viên")
            @RequestParam(required = false)
            Long packageId) {
        ResponsePage<GetListBenefitOutput> responsePage = packageService.getPageBenefit(packageId,super.pageable(pageNo, pageSize, sort));
        return ResponseUtils.success(responsePage);
    }


    @Operation(summary = "Api nhận kết quả đăng ký gói hội viên")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @PostMapping("/internal/package/purchase-package")
    public ResponseEntity<ResponseData<String>> purchasePackage(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Valid @RequestBody
            PurchasePackageInput purchasePackageInput) {
        ;
        return ResponseUtils.success(packageService.purchasePackage(purchasePackageInput));
    }


    @Operation(summary = "Api gói hôi viên của khách hàng")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/package/registed-package")
    public ResponseEntity<ResponseData<GetListPackageOutput>> getRegistedPackage(@Parameter(
            description = "Chuỗi xác thực khi gọi api nội bộ",
            example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "Mã cif no core bank")
            @RequestParam(required = false)
            String cifNo) {
        GetListPackageOutput lstResponse = packageService.getRegistedPackage(cifNo);
        return ResponseUtils.success(lstResponse);
    }

}
