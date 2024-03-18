package vn.com.atomi.loyalty.core.dto.projection;

/**
 * @author haidv
 * @version 1.0
 */
public interface CustomerBalanceProjection {

  String getCustomerName();

  String getCifBank();

  String getCifWallet();

  Long getCustomerId();

  Long getTotalAmount();

  Long getLockAmount();

  Long getAvailableAmount();
}
