package vn.com.atomi.loyalty.core.utils;

/**
 * @author haidv
 * @version 1.0
 */
public class Constants {

  public static final String EXPIRED_POINT_CONTENT = "Thực hiện hết hạn điểm cuối ngày";

  public static final String DICTIONARY_UNIQUE_TYPE = "UNIQUE_TYPE";

  public static final String DICTIONARY_LIMIT_POINT_PER_USER = "LIMIT_POINT_PER_USER";
  public static final String DICTIONARY_GENDER = "GENDER";
  public static final String DICTIONARY_CUSTOMER_TYPE = "CUSTOMER_TYPE";
  public static final String DICTIONARY_NATIONALITY = "NATIONALITY";

  public static final String DICTIONARY_RULE_TYPE_TRANSACTION = "TRANSACTION";

  public static final String DEFAULT_DAY_START_TIME = "00:00:00";

  public static final String DEFAULT_DAY_END_TIME = "23:59:59";
  public static final String LOCATION_VN = "VN";

  public class Status {
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
  }
  public class Mission {
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_INPROGRESS = "INPROGRESS";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String TYPE_CHAIN = "C";
    public static final String TYPE_GROUP = "G";
    public static final String TYPE_MISSION = "M";

  }
  public class Notification {
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String MISSION_TITLE = "Đăng ký chuỗi nhiệm vụ thành công";
    public static final String MISSION_CONTENT = "Quý khách đã đăng ký thành công chuỗi nhiệm vụ";
    public static final String PACKAGE_TITLE = "Đăng ký gói hội viên thành công";
    public static final String PACKAGE_CONTENT = "Đăng ký gói hội viên thành công";
    public static final String POINT_TITLE = "Tài khoản điểm loyalty";
    public static final String POINT_CONTENT = "Số điểm /a Số dư tài khoản điểm /b";

  }


  private Constants() {
    throw new IllegalStateException("Utility class");
  }
}
