package vn.com.atomi.loyalty.core.dto.output;

import lombok.Data;

import java.util.Date;

@Data
public class EGCBiometricOutput {

    private Integer id;

    private String customerName;

    private String cifBank;

    private String phone;

    private String uniqueType;

    private String uniqueValue;

    private Date completeAt;

    private Boolean isPlusPoint;

}
