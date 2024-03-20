package vn.com.atomi.loyalty.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.core.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.core.enums.Status;
import vn.com.atomi.loyalty.core.service.MemberService;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class MemberController extends BaseController {
  private final MemberService memberService;

  @GetMapping
  @Operation(summary = "Api lấy danh sách thành viên")
  @PreAuthorize(Authority.Customer.READ_CUSTOMER_ACCOUNT)
  public ResponseEntity<ResponseData<ResponsePage<CustomerOutput>>> getCustomerPointAccounts(
      @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
          Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "ID khách hàng bên loyalty") @RequestParam(required = false)
          Long customerId,
      @Parameter(description = "Tên khách hàng bên loyalty") @RequestParam(required = false)
          String customerName,
      @Parameter(description = "Mã định danh của khách hàng trên bank")
          @RequestParam(required = false)
          String cifBank,
      @Parameter(description = "Xếp hạng KH") @RequestParam(required = false) String rank,
      @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
          @RequestParam(required = false)
          Status status,
      @Parameter(description = "Phân khúc KH") @RequestParam(required = false) String segment) {

    return ResponseUtils.success(
        memberService.gets(
            status,
            customerId,
            customerName,
            cifBank,
            rank,
            segment,
            super.pageable(pageNo, pageSize, sort)));
  }
}
