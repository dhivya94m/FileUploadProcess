package com.org.bchio.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDetailsDto {

	private String fileName;
	private String fileType;
	private Long size;
	private String checksum;
	private Integer status;
	private Date processStartDT;
	private Date dataCollectionDT;
	private Date processEndDT;
	private Integer totalRcds;
	private Integer totalValidRcds;
	private Integer totalInValidRcds;
	private Date summaryStartDT;
	private Date summaryEndDT;

	private List<TransactionSummaryDto> transactionsSummary;

}
