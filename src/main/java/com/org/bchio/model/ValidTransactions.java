package com.org.bchio.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ValidTransactions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TRANSACTION_ID", nullable = false, unique = true)
	private Integer transactionId;
	@Column(name = "DEAL_UNIQUE_ID", nullable = false)
	private Integer dealUniqueId;
	@Column(name = "ORDERING_CURRENCY", nullable = false)
	private Integer orderingCurrency;
	@Column(name = "SETTLING_CURRENCY", nullable = false)
	private Integer settlingCurrency;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEAL_TIMESTAMP", nullable = false)
	private Date dealTimestamp;
	@Column(name = "DEAL_AMOUNT", nullable = false)
	private Double dealAmount;

	@ManyToOne
	@JoinColumn(name = "FILE_NAME")
	private FileDetails fileDetails;

}
