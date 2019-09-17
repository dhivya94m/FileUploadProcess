package com.org.bchio.service.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.org.bchio.model.FileDetails;
import com.org.bchio.model.InvalidTransactions;
import com.org.bchio.model.TransactionsSummary;
import com.org.bchio.model.ValidTransactions;
import com.org.bchio.properties.FileStorageProperties;
import com.org.bchio.repository.InvalidTransactionRepository;
import com.org.bchio.repository.ValidTransactionRepository;
import com.org.bchio.service.component.DataMapperComponent;

@Service
@Transactional
public class FileUploadEventService {

	Logger logger = LoggerFactory.getLogger(FileUploadEventService.class);

	@Autowired
	private DataMapperComponent mapperComponent;
	@PersistenceContext
	private EntityManager entityManager;
	private static final int BATCH_SIZE = 50;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	@Autowired
	private ValidTransactionRepository validTransactionRepository;
	@Autowired
	private InvalidTransactionRepository invalidTransactionRepository;

	public boolean loadDataWithRepository(FileDetails fd) throws Exception {
		File inFile = null;
		boolean status = false;
		List<Object> dataList = null;
		List<ValidTransactions> validTransactions = null;
		List<InvalidTransactions> invalidTransactions = null;
		try {
			inFile = ResourceUtils.getFile(fileStorageProperties.getUploadDir() + fd.getFileName());
			try (InputStream inFileStream = new FileInputStream(inFile)) {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(inFileStream))) {
					logger.info("Start: " + new Date());
					dataList = br.lines().map(line -> mapperComponent.mapCSVToObject(fd, line))
							.collect(Collectors.toList());
					int size = dataList.size();
					logger.info("Full List Collected: " + new Date() + " Size: " + size);
					validTransactions = dataList.stream().map(obj -> mapperComponent.mapValidTransactions(obj))
							.filter(obj -> obj != null).collect(Collectors.toList());
					logger.info("Valid List Collected: " + new Date() + " Size: " + validTransactions.size());
					invalidTransactions = dataList.stream().map(obj -> mapperComponent.mapInValidTransactions(obj))
							.filter(obj -> obj != null).collect(Collectors.toList());
					System.out
							.println("InValid List Collected: " + new Date() + " Size: " + invalidTransactions.size());
					dataList.clear();
					dataList = null;
					validTransactionRepository.saveAll(validTransactions);
					logger.info("Valid Batch Inserted: " + new Date());
					invalidTransactionRepository.saveAll(invalidTransactions);
					logger.info("InValid Batch Inserted: " + new Date());
					logger.info("End: " + new Date());
					status = true;
				}
			}
		} finally {
			inFile = null;
			dataList = null;
			validTransactions = null;
			invalidTransactions = null;
		}
		return status;

	}

	public boolean loadDataWithEntityManager(FileDetails fd) throws Exception {
		File inFile = null;
		boolean status = false;
		List<Object> dataList = null;
		List<TransactionsSummary> transactionSummaryList = null;
		try {
			inFile = ResourceUtils.getFile(fileStorageProperties.getUploadDir() + fd.getFileName());
			try (InputStream inFileStream = new FileInputStream(inFile)) {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(inFileStream))) {
					fd.setProcessStartDT(new Date());
					logger.info("File Upload Process Start: " + fd.getProcessStartDT());
					dataList = br.lines().map(line -> mapperComponent.mapCSVToObject(fd, line))
							.collect(Collectors.toList());
					int size = dataList.size();
					fd.setDataCollectionDT(new Date());
					fd.setTotalRcds(size);
					logger.info("Full List Collected: " + fd.getDataCollectionDT() + " Size: " + fd.getTotalRcds());
					for (int i = 0; i < size; i++) {
						if (i > 0 && i % BATCH_SIZE == 0) {
							entityManager.flush();
							entityManager.clear();
						}
						entityManager.persist(dataList.get(i));
					}
					fd.setProcessEndDT(new Date());
					logger.info("File Upload Process End: " + fd.getProcessEndDT());
					fd.setSummaryStartDT(new Date());
					logger.info("Summary Start: " + fd.getSummaryStartDT());
					fd.setTotalValidRcds(getNoOfValidRecords(dataList));
					fd.setTotalInValidRcds(getNoOfInValidRecords(dataList));
					transactionSummaryList = validTransactionRepository.getSummaryData(fd.getFileName()).stream()
							.map(dto -> mapperComponent.mapTransactionsSummary(dto, fd)).collect(Collectors.toList());
					if (transactionSummaryList != null && !transactionSummaryList.isEmpty()) {
						for (int i = 0; i < transactionSummaryList.size(); i++) {
							entityManager.persist(transactionSummaryList.get(i));
						}
					}
					fd.setSummaryEndDT(new Date());
					logger.info("Summary Start: " + fd.getSummaryEndDT());
					status = true;
				}
			}
		} finally {
			inFile = null;
			dataList = null;
		}
		return status;

	}

	private Integer getNoOfValidRecords(List<Object> dataList) {
		return dataList.stream().map(obj -> mapperComponent.mapValidTransactions(obj)).filter(obj -> obj != null)
				.collect(Collectors.toList()).size();
	}

	private Integer getNoOfInValidRecords(List<Object> dataList) {
		return dataList.stream().map(obj -> mapperComponent.mapInValidTransactions(obj)).filter(obj -> obj != null)
				.collect(Collectors.toList()).size();
	}

	public boolean loadDataWithEntityManagerNativeQuery(FileDetails fd) throws Exception {
		File inFile = null;
		boolean status = false;
		List<Object> dataList = null;
		List<String> validTransactions = null;
		List<String> invalidTransactions = null;
		try {
			inFile = ResourceUtils.getFile(fileStorageProperties.getUploadDir() + fd.getFileName());
			try (InputStream inFileStream = new FileInputStream(inFile)) {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(inFileStream))) {
					logger.info("Start: " + new Date());
					dataList = br.lines().map(line -> mapperComponent.mapCSVToObject(fd, line))
							.collect(Collectors.toList());
					int size = dataList.size();
					logger.info("Full List Collected: " + new Date() + " Size: " + size);
					validTransactions = dataList.stream().map(obj -> mapperComponent.mapValidTransactionsQuery(obj))
							.filter(obj -> obj != null).collect(Collectors.toList());
					logger.info("Valid List Collected: " + new Date() + " Size: " + validTransactions.size());
					invalidTransactions = dataList.stream().map(obj -> mapperComponent.mapInValidTransactionsQuery(obj))
							.filter(obj -> obj != null).collect(Collectors.toList());
					System.out
							.println("InValid List Collected: " + new Date() + " Size: " + invalidTransactions.size());
					dataList.clear();
					dataList = null;
					System.out
							.println("Valid Batch Inserted: " + new Date() + StringUtils.join(validTransactions, ","));
					logger.info("InValid Batch Inserted: " + new Date() + StringUtils.join(invalidTransactions, ","));
					logger.info("End: " + new Date());
					status = true;
				}
			}
		} finally {
			inFile = null;
			dataList = null;
			validTransactions = null;
			invalidTransactions = null;
		}
		return status;

	}

}
