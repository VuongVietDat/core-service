package vn.com.atomi.loyalty.core.dto.output;

import lombok.Data;

import java.util.Date;

@Data
public class EGLoginOutput {

    private Integer id;

    private String customerName;

    private String cifBank;

    private String phone;

    private Date loginAt;
}
