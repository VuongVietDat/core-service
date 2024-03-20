package vn.com.atomi.loyalty.core.dto.projection;

import java.time.LocalDate;

/**
 * @author haidv
 * @version 1.0
 */
public interface CustomerPointAccountProjection {

  Long getCustomerId();

  String getCustomerName();

  String getUniqueValue();

  String getUniqueType();

  String getCifBank();

  String getCifWallet();

  Long getTotalAmount();

  Long getLockAmount();

  Long getAvailableAmount();

  String getRank();

  Long getRankPoint();

  Long getPointAboutExpire();

  Long getTotalPointsUsed();

  LocalDate getMostRecentExpirationDate();
}
