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

  private Constants() {
    throw new IllegalStateException("Utility class");
  }
}
