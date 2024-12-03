package vn.com.atomi.loyalty.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.core.dto.input.PurchaseChainMissionInput;
import vn.com.atomi.loyalty.core.dto.output.CChainMissionOuput;
import vn.com.atomi.loyalty.core.dto.output.CMissionOuput;
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

    @Operation(summary = "Api lấy danh sách chuỗi nhiệm vụ")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/mission/get-new-chain-mission")
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

    @Operation(summary = "Api lấy danh sách chuỗi nhiệm vụ đã đăng ký INPROGRESS|COMPLETED")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/mission/get-registed-chain-mission")
    public ResponseEntity<ResponseData<List<CChainMissionOuput>>> getRegistedChainMission(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "Mã định danh gói hội viên")
            @RequestParam(required = true)
            String cifNo,
            @Parameter(description = "Trạng thái")
            @RequestParam(required = true)
            String status) {
        List<CChainMissionOuput> lstResponse = missionService.getRegistedChainMission(cifNo, status);
        return ResponseUtils.success(lstResponse);
    }

    @Operation(summary = "Api chi tiết chuỗi nhiệm vụ")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/mission/get-chain-mission-detail")
    public ResponseEntity<ResponseData<CChainMissionOuput>> getChainMissionDetail(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "Mã định danh gói hội viên")
            @RequestParam(required = true)
            Long id) {
        CChainMissionOuput lstResponse = missionService.getChainMissionDetail(id);
        return ResponseUtils.success(lstResponse);
    }

    @Operation(summary = "Api lấy danh sách nhiệm vụ")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/mission/get-list-mission")
    public ResponseEntity<ResponseData<List<CMissionOuput>>> getListMission(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "Mã định danh gói hội viên")
            @RequestParam(required = true)
            Long chainId) {
        List<CMissionOuput> lstResponse = missionService.getListMission(chainId);
        return ResponseUtils.success(lstResponse);
    }
    @Operation(summary = "Api lấy danh sách nhiệm vụ đang thực hiện")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/mission/get-list-mission-inprogress")
    public ResponseEntity<ResponseData<List<CMissionOuput>>> getListMissionInProgress(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "Mã cifNo")
            @RequestParam(required = true)
            String cifNo,
            @Parameter(description = "Mã định danh gói hội viên")
            @RequestParam(required = true)
            Long chainId) {
        List<CMissionOuput> lstResponse = missionService.getListMissionInProgress(cifNo, chainId);
        return ResponseUtils.success(lstResponse);
    }
    @Operation(summary = "Api chi tiết nhiệm vụ")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/mission/get-mission-detail")
    public ResponseEntity<ResponseData<CMissionOuput>> getMissionDetail(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "Mã định danh gói hội viên")
            @RequestParam(required = true)
            Long id) {
        CMissionOuput lstResponse = missionService.getMissionDetail(id);
        return ResponseUtils.success(lstResponse);
    }

    @Operation(summary = "Api đăng ký chuỗi nhiệm vụ")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @PostMapping("/internal/mission/purchase-mission")
    public ResponseEntity<ResponseData<String>> purchaseChainMission(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Valid @RequestBody
            PurchaseChainMissionInput purchaseChainMission) {
        return ResponseUtils.success(missionService.purchaseChainMission(purchaseChainMission));
    }

    @Operation(summary = "Api đăng ký chuỗi nhiệm vụ")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @PostMapping("/internal/mission/finish-mission")
    public ResponseEntity<ResponseData<Void>> finishMission(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "Mã định danh nhiệm vụ")
            @RequestParam(required = true)
            Long missionId,
            @Parameter(description = "Mã định danh chuỗi nhiệm vụ")
            @RequestParam(required = true)
            Long chainId,
            @Parameter(description = "Số CIF NO")
            @RequestParam(required = true)
            String cifNo) {
            missionService.finishMission(missionId, chainId, cifNo);
        return ResponseUtils.success();
    }

}
