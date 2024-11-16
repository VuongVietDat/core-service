package vn.com.atomi.loyalty.core.dto.output;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.core.enums.Status;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "C_CURRENCY_TRANSACTION")
public class CurrencyTransaction extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "C_CURRENCY_TRANSACTION_ID_SEQ")
	@SequenceGenerator(name = "C_CURRENCY_TRANSACTION_ID_SEQ", sequenceName = "C_CURRENCY_TRANSACTION_ID_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "CIF_BANK")
	private String cifBank;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	
	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;
	
	@Column(name = "CURRENCY_UNIT")
	private String currencyUnit;
	
	@Column(name = "CURRENCY_AMOUNT")
	private long currencyAmount;
	
	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;
	
	
	@Column(name = "CURRENCY_EXCHANGE_RATE")
	private double currencyExchangeRate;
	
	@Column(name = "VND_AMOUNT")
	private long vndAmount;
	
	@Column(name = "TRANSACTION_DATE")
	private LocalDate transactionDate;
	
	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private Status status;
}
