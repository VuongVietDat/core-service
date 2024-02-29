package vn.com.atomi.loyalty.core.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
@AllArgsConstructor
public class LoginInput {

  private String clientId;

  private String clientSecret;
}
