package vn.com.atomi.loyalty.core.dto.message;

import lombok.*;
import vn.com.atomi.loyalty.core.dto.input.AllocationPointTransactionInput;
import vn.com.atomi.loyalty.core.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.core.enums.RuleType;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AllocationPointMessage {

  private CustomerOutput customer;

  private AllocationPointTransactionInput transaction;

  private RuleType type;
}
