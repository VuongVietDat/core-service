package vn.com.atomi.loyalty.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.core.dto.input.PartnerInput;
import vn.com.atomi.loyalty.core.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.core.dto.output.PartnersOutput;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.service.PartnerService;

@RestController
@RequiredArgsConstructor
public class PartnerController extends BaseController {
    private final PartnerService partnerService;

    @GetMapping("/partners")
    @Operation(summary = "Api lấy danh sách đối tác cung cấp")
    @PreAuthorize(Authority.Customer.READ_CUSTOMER_ACCOUNT)
    public ResponseEntity<ResponseData<ResponsePage<PartnersOutput>>> getListPartners(
            @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
            Integer pageNo,
            @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
            Integer pageSize,
            @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
            @RequestParam(required = false)
            String sort,
            @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
            @RequestParam(required = false)
            Status status,
            @Parameter(description = "Từ khóa tìm kiếm theo mã/tên đối tác")
            @RequestParam(required = false)
            String keyword
    ){
        return ResponseUtils.success(
                partnerService.getListPartners(status, keyword, super.pageable(pageNo, pageSize, sort)));
    }

    @Operation(summary = "APi tạo mới đối tác")
    @PostMapping("/partner")
    public ResponseEntity<ResponseData<Void>> createPartner(
            @RequestBody @Valid PartnerInput partnerInput) {
        partnerService.createPartner(partnerInput);
        return ResponseUtils.success();
    }

    @Operation(summary = "Api chỉnh sửa đối tác")
    @PutMapping("/partner/{id}")
    public ResponseEntity<ResponseData<Void>> updatePartner(
            @Parameter(description = "ID bản ghi quà") @PathVariable Long id,
            @RequestBody PartnerInput partnerInput) {
        partnerService.update(id, partnerInput);
        return ResponseUtils.success();
    }

    @Operation(summary = "Api lấy chi tiết bản ghi đối tác theo id")
    @GetMapping("/partner/{id}")
    public ResponseEntity<ResponseData<PartnersOutput>> get(
            @Parameter(description = "ID bản ghi đối tác") @PathVariable Long id) {
        return ResponseUtils.success(partnerService.get(id));
    }
}
