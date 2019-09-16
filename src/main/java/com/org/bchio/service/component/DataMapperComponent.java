package com.org.bchio.service.component;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.org.bchio.dto.TransactionSummaryDto;
import com.org.bchio.model.FileDetails;
import com.org.bchio.model.InvalidTransactions;
import com.org.bchio.model.TransactionsSummary;
import com.org.bchio.model.ValidTransactions;

@Component
public class DataMapperComponent {

	Logger logger = LoggerFactory.getLogger(DataMapperComponent.class);

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	public Object mapCSVToObject(FileDetails fd, String line) {
		String[] record = null;
		// FileDetails fd = null;
		try {
			if (line != null && !line.isEmpty()) {
				// fd = new FileDetails(fileName);
				record = line.split("\\,");
				if (record.length == 5) {
					try {
						if (record[0] != null && !record[0].isEmpty() && record[0].matches("-?\\d+(\\.\\d+)?")) {
							if (record[1] != null && !record[1].isEmpty() && record[1].matches("-?\\d+(\\.\\d+)?")) {
								if (record[2] != null && !record[2].isEmpty()
										&& record[2].matches("-?\\d+(\\.\\d+)?")) {
									if (record[3] != null && !record[3].isEmpty()) {
										if (record[4] != null && !record[4].isEmpty()
												&& record[4].matches("-?\\d+(\\.\\d+)?")) {
											return new ValidTransactions(IDGenerator.getId(),
													Integer.parseInt(record[0]), Integer.parseInt(record[1]),
													Integer.parseInt(record[2]), simpleDateFormat.parse(record[3]),
													Double.parseDouble(record[4]), fd);
										}
									}
								}
							}

						}
					} catch (Exception ex) {
						logger.error("MAPPER Error 1: " + ex);
						return new InvalidTransactions(IDGenerator.getId(), line, fd);
					}
				} else {
					return new InvalidTransactions(IDGenerator.getId(), line, fd);
				}
			}
		} catch (Exception ex) {
			logger.error("MAPPER Error 2: " + ex);
		} finally {
			record = null;
			fd = null;
		}
		return new InvalidTransactions(IDGenerator.getId(), line, fd);
	}

	public TransactionsSummary mapTransactionsSummary(TransactionSummaryDto dto, FileDetails fd) {
		TransactionsSummary transactionsSummary = null;
		try {
			transactionsSummary = new TransactionsSummary();
			transactionsSummary.setSummaryId(IDGenerator.getId());
			transactionsSummary.setFileDetails(fd);
			BeanUtils.copyProperties(dto, transactionsSummary);
			return transactionsSummary;
		} finally {
			transactionsSummary = null;
		}
	}

	public ValidTransactions mapValidTransactions(Object obj) {
		return (obj instanceof ValidTransactions) ? (ValidTransactions) obj : null;
	}

	public InvalidTransactions mapInValidTransactions(Object obj) {
		return (obj instanceof InvalidTransactions) ? (InvalidTransactions) obj : null;
	}

	public String mapValidTransactionsQuery(Object obj) {
		ValidTransactions vt = null;
		StringBuilder sb = null;
		try {
			vt = (ValidTransactions) obj;
			sb = new StringBuilder();
			sb.append("(").append(vt.getTransactionId()).append(",").append(vt.getDealUniqueId()).append(",")
					.append(vt.getOrderingCurrency()).append(",").append(vt.getSettlingCurrency()).append(",")
					.append("TO_TIMESTAMP('dd/mm/yyyy HH:mm:ss','")
					.append(simpleDateFormat.format(vt.getDealTimestamp())).append("')").append(",")
					.append(vt.getDealAmount()).append(")");
			return sb.toString();
		} catch (Exception ex) {
			return null;
		} finally {
			vt = null;
			sb = null;
		}
	}

	public String mapInValidTransactionsQuery(Object obj) {
		InvalidTransactions ivt = null;
		StringBuilder sb = null;
		try {
			ivt = (InvalidTransactions) obj;
			sb = new StringBuilder();
			sb.append("(").append(ivt.getTransactionId()).append(",").append("'").append(ivt.getActualRecord())
					.append("'").append(")");
			return sb.toString();
		} catch (Exception ex) {
			return null;
		} finally {
			ivt = null;
			sb = null;
		}
	}

}
