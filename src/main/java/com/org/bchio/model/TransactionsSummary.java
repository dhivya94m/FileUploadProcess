package com.org.bchio.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TransactionsSummary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SUMMARY_ID", nullable = false, unique = true)
	private Integer summaryId;
	@Column(name = "ORDERING_CURRENCY", nullable = false)
	private Integer orderingCurrency;
	@Column(name = "DEAL_AMOUNT", nullable = false)
	private Double dealAmount;

	@ManyToOne
	@JoinColumn(name = "FILE_NAME")
	private FileDetails fileDetails;

}
