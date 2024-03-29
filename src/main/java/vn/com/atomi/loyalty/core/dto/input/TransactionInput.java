package vn.com.atomi.loyalty.core.dto.input;

import java.time.LocalDateTime;
import lombok.*;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionInput {

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
