package vn.com.atomi.loyalty.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import vn.com.atomi.loyalty.core.enums.RefType;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TRANS_EXTERNAL")
public class TransExternal {
    @Id
    @Column(name = "ID", nullable = false, length = 36)
    private String id;

    @Column(name = "REF_ID")
    private Long refId;


    @Column(name = "REF_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private RefType refType;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "PHONE_NO", length = 13)
    private String phoneNo;

    @Column(name = "CIF_NO", length = 22)
    private String cifNo;

    @NotNull
    @Column(name = "TXN_REF_NO", nullable = false, length = 50)
    private String txnRefNo;

    @NotNull
    @Column(name = "TXN_ID", nullable = false, length = 50)
    private String txnId;

    @Column(name = "TXN_AMOUNT")
    private Long txnAmount;

    @NotNull
    @Nationalized
    @Column(name = "TXN_STATUS", nullable = false, length = 10)
    private String txnStatus;

    @Column(name = "TXN_CURRENCY", length = 3, columnDefinition = "CHAR(3)")
    private String txnCurrency;

    @Nationalized
    @Column(name = "TXN_METHOD", length = 50)
    private String txnMethod;

    @NotNull
    @Column(name = "TXN_CHANNEL", nullable = false, length = 10)
    private String txnChannel;

    @NotNull
    @Column(name = "TXN_DATE", nullable = false)
    private LocalDate txnDate;

    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    @Column(name = "EXPIRED_DATE")
    private LocalDate expiredDate;

    @Nationalized
    @Column(name = "TXN_NOTE", length = 200)
    private String txnNote;

}