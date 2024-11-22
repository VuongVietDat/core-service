package vn.com.atomi.loyalty.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.core.dto.output.CChainMissionOuput;
import vn.com.atomi.loyalty.core.dto.output.GetListBenefitOutput;
import vn.com.atomi.loyalty.core.service.MissionService;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class MissionController extends BaseController {

    private final MissionService missionService;

    @Operation(summary = "Api lấy danh sách nhiệm vụ")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/mission/new-chain-mission")
    public ResponseEntity<ResponseData<List<CChainMissionOuput>>> getNewChainMission(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey
    ) {
        List<CChainMissionOuput> lstResponse = missionService.getNewChainMission();
        return ResponseUtils.success(lstResponse);
    }

    @Operation(summary = "Api lấy nhiệm vụ đang thực hiện")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/mission/mission-inprogress")
    public ResponseEntity<ResponseData<List<CChainMissionOuput>>> getMissionInProgress(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "Mã định danh gói hội viên")
            @RequestParam(required = false)
            String cifNo) {
        List<CChainMissionOuput> lstResponse = missionService.getMissionInProgress(cifNo);
        return ResponseUtils.success(lstResponse);
    }
//
//    @Operation(summary = "Api nhận kết quả đăng ký gói hội viên")
//    @PreAuthorize(Authority.ROLE_SYSTEM)
//    @PostMapping("/internal/package/purchase-package")
//    public ResponseEntity<ResponseData<Void>> purchasePackage(
//            @Parameter(
//                    description = "Chuỗi xác thực khi gọi api nội bộ",
//                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
//            @RequestHeader(RequestConstant.SECURE_API_KEY)
//            @SuppressWarnings("unused")
//            String apiKey,
//            @Valid @RequestBody PurchasePackageInput purchasePackageInput) {
//        packageService.purchasePackage(purchasePackageInput);
//        return ResponseUtils.success();
//    }
//
//    @Operation(summary = "Api gói hôi viên của khách hàng")
//    @PreAuthorize(Authority.ROLE_SYSTEM)
//    @GetMapping("/internal/package/registed-package")
//    public ResponseEntity<ResponseData<RegistedPackageOuput>> getRegistedPackage(@Parameter(
//            description = "Chuỗi xác thực khi gọi api nội bộ",
//            example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
//            @RequestHeader(RequestConstant.SECURE_API_KEY)
//            @SuppressWarnings("unused")
//            String apiKey,
//            @Parameter(description = "Mã cif no core bank")
//            @RequestParam(required = false)
//            String cifNo) {
//        RegistedPackageOuput lstResponse = packageService.getRegistedPackage(cifNo);
//        return ResponseUtils.success(lstResponse);
//    }

}
