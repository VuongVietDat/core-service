package vn.com.atomi.loyalty.core.dto.message;

import java.time.LocalDateTime;
import lombok.*;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AllocationPointTransactionInput {

  private String refNo;

  private Long amount;

  private LocalDateTime transactionAt;

  private String productType;

  private String productLine;

  private String currency;

  private String chanel;

  private String transactionGroup;

  private String transactionType;
}
