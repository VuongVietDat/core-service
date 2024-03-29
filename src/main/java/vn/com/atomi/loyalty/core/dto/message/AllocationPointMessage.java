package vn.com.atomi.loyalty.core.dto.message;

import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.core.dto.input.AllocationPointTransactionInput;
import vn.com.atomi.loyalty.core.dto.output.CustomerOutput;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class AllocationPointMessage {

  private CustomerOutput customer;

  private AllocationPointTransactionInput transaction;

  private String type;
}
