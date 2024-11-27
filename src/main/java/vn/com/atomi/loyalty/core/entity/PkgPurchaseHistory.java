package vn.com.atomi.loyalty.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PKG_PURCHASE_HISTORY")
public class PkgPurchaseHistory {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PKG_PURCHASE_HISTORY_ID_SEQ")
    @SequenceGenerator(
            name = "PKG_PURCHASE_HISTORY_ID_SEQ",
            sequenceName = "PKG_PURCHASE_HISTORY_ID_SEQ",
            allocationSize = 1)
    private Long id;
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    @Column(name = "CIF_NO")
    private String cifNo;
    @Column(name = "PACKAGE_ID")
    private Long packageId;

    @Column(name = "REF_NO")
    private String refNo;

    @Column(name = "TXN_ID")
    private String transId;

    @Column(name = "PURCHASED_DATE")
    private LocalDate purchasedDate;

    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    @Column(name = "EXPIRED_DATE")
    private LocalDate expiredDate;

    @Column(name = "TXN_AMOUNT")
    private Long txnAmount;

    @Column(name = "TXN_STATUS")
    private String txnStatus;

    @Column(name = "TXN_CURRENCY" ,columnDefinition = "CHAR(3)")
    private String txnCurrency;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "PAYMENT_CHANNEL")
    private String paymentChannel;
}
