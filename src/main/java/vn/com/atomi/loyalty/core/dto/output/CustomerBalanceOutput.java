package vn.com.atomi.loyalty.core.dto.output;

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
public class CustomerBalanceOutput {

  private String customerName;

  private String cifBank;

  private String cifWallet;

  private Long customerId;

  private Long totalAmount;

  private Long lockAmount;

  private Long availableAmount;

  private String note;
}
