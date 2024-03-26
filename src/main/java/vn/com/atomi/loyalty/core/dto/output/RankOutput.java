package vn.com.atomi.loyalty.core.dto.output;

import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.core.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class RankOutput {

  private Long id;

  private String code;

  private String name;

  private Status status;

  private Integer order;
}
