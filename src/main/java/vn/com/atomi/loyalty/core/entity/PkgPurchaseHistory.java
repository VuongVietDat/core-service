package vn.com.atomi.loyalty.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

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
    private Long refNo;

    @Column(name = "TRANS_ID")
    private Long transId;

    @Column(name = "PURCHASED_DATE")
    private Date purchasedDate;
    @Column(name = "EFFECTIVE_DATE")
    private Date effectiveDate;
    @Column(name = "EXPIRED_DATE")
    private Date expiredDate;
    @Column(name = "TXN_AMOUNT")
    private Long txnAmount;
    @Column(name = "TXN_STATUS")
    private String txnStatus;
    @Column(name = "TXN_CURRENCY")
    private String txnCurrency;
    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;
    @Column(name = "PAYMENT_CHANNEL")
    private String paymentChannel;
}
