package com.org.bchio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FILE_DETAILS")
public class FileDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "FILE_NAME", nullable = false, unique = true)
	private String fileName;
	@Column(name = "FILE_TYPE", nullable = false)
	private String fileType;
	@Column(name = "FILE_SIZE", nullable = false)
	private Long size;
	@Column(name = "CHECKSUM", nullable = false)
	private String checksum;
	@Column(name = "STATUS", nullable = false)
	private Integer status;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESS_START_DT")
	private Date processStartDT;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_COLLECTION_DT")
	private Date dataCollectionDT;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESS_END_DT")
	private Date processEndDT;
	@Column(name = "TOTAL_RCDS")
	private Integer totalRcds;
	@Column(name = "TOTAL_VALID_RCDS")
	private Integer totalValidRcds;
	@Column(name = "TOTAL_INVALID_RCDS")
	private Integer totalInValidRcds;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SUMMARY_START_DT")
	private Date summaryStartDT;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SUMMARY_END_DT")
	private Date summaryEndDT;

	@OneToMany(mappedBy = "fileDetails")
	private List<ValidTransactions> validTransactionsList = new ArrayList<ValidTransactions>(1);

	@OneToMany(mappedBy = "fileDetails")
	private List<InvalidTransactions> invalidTransactionsList = new ArrayList<InvalidTransactions>(1);

	@OneToMany(mappedBy = "fileDetails")
	private List<TransactionsSummary> transactionsSummary = new ArrayList<TransactionsSummary>(1);

	public FileDetails(String fileName) {
		super();
		this.fileName = fileName;
	}
}
