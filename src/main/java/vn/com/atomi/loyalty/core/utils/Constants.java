package vn.com.atomi.loyalty.core.utils;

/**
 * @author haidv
 * @version 1.0
 */
public class Constants {

  public static final String NOTE_CUSTOMER_NOT_FOUND = "Customer not found in loyalty system";

  public static final String DEFAULT_DAY_START_TIME = "00:00:00";

  public static final String DEFAULT_DAY_END_TIME = "23:59:59";

  private Constants() {
    throw new IllegalStateException("Utility class");
  }
}
