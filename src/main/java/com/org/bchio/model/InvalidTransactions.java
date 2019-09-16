package com.org.bchio.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "INVALID_TRANSACTIONS")
public class InvalidTransactions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TRANSACTION_ID", nullable = false, unique = true)
	private Integer transactionId;
	@Column(name = "ACTUAL_RECORD", nullable = false)
	private String actualRecord;

	@ManyToOne
	@JoinColumn(name = "FILE_NAME")
	private FileDetails fileDetails;

}
