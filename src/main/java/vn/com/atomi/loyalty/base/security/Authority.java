package vn.com.atomi.loyalty.base.security;

/**
 * @author haidv
 * @version 1.0
 */
public class Authority {

  public static final String ROLE_SYSTEM = "hasAuthority('ROLE_SYSTEM')";

  public static class CustomerGroup {
    public static final String CREATE_CUSTOMER_GROUP = "hasAuthority('CREATE_CUSTOMER_GROUP')";
    public static final String UPDATE_CUSTOMER_GROUP = "hasAuthority('UPDATE_CUSTOMER_GROUP')";
    public static final String READ_CUSTOMER_GROUP = "hasAuthority('READ_CUSTOMER_GROUP')";
    public static final String APPROVE_CUSTOMER_GROUP = "hasAuthority('APPROVE_CUSTOMER_GROUP')";
  }

  public static class Customer {
    public static final String READ_CUSTOMER_ACCOUNT = "hasAuthority('READ_CUSTOMER_ACCOUNT')";
    public static final String READ_BALANCE_HISTORY = "hasAuthority('READ_BALANCE_HISTORY')";
  }
}
