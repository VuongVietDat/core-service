package vn.com.atomi.loyalty.core.enums;

import org.springframework.http.HttpStatus;
import vn.com.atomi.loyalty.base.exception.AbstractError;

/**
 * @author haidv
 * @version 1.0
 */
public enum ErrorCode implements AbstractError {
  INPUT_INVALID(
      4000, "Bắt buộc truyền một trong 2 input (cifBank, cifWallet).", HttpStatus.BAD_REQUEST),
  CUSTOMER_GROUP_NOT_EXISTED(4001, "Không tồn tại nhóm khách hàng.", HttpStatus.NOT_FOUND),

  APPROVING_RECORD_NOT_EXISTED(4002, "Không tìm thấy bản ghi chờ duyệt.", HttpStatus.NOT_FOUND),
  EXISTED_CAMPAIGN_USE_CUSTOMER_GROUP(
      4003, "Nhóm khách hàng này đang được sử dụng ở chiến dịch.", HttpStatus.BAD_REQUEST),
  CUSTOMER_NOT_EXISTED(4004, "Khách hàng không tồn tại.", HttpStatus.BAD_REQUEST),
  NOT_ENOUGH_BALANCE(4005, "Bạn không đủ số dư để thực hiện thao tác.", HttpStatus.BAD_REQUEST),
  EXISTED_PARTNER_CODE(4006, "Mã đối tác đã tồn tại", HttpStatus.BAD_REQUEST),
  PARTNER_NOT_EXISTED(4007, "Đối tác không tồn tại", HttpStatus.BAD_REQUEST),
  PACKAGE_NOT_EXISTED(5001, "Không tồn tại gói hội viên.", HttpStatus.NOT_FOUND),
  CUSTOMER_REGISTED_PACKAGE(5002, "Khách hàng đã đăng ký gói hội viên.", HttpStatus.BAD_REQUEST),
  CUSTOMER_BALANCE_NOT_FOUND(6001, "Không tìm được số dư.", HttpStatus.BAD_REQUEST),
  CUSTOMER_BALANCE_NOT_ENOUGH(6001, "Số dư của bạn không đủ để thực hiện.", HttpStatus.BAD_REQUEST),
  CUSTOMER_REGISTED_CHAIN_MISSION(6002, "Khách hàng đã đăng ký gói nhiệm vụ.", HttpStatus.BAD_REQUEST);
;

  private final int code;

  private final String message;

  private final HttpStatus httpStatus;

  ErrorCode(int code, String message, HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
