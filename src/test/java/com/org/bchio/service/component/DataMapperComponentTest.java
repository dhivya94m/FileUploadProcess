package com.org.bchio.service.component;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.org.bchio.constants.GenericConstants;
import com.org.bchio.dto.TransactionSummaryDto;
import com.org.bchio.model.FileDetails;
import com.org.bchio.model.InvalidTransactions;
import com.org.bchio.model.ValidTransactions;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataMapperComponentTest {

	@Autowired
	private DataMapperComponent dataMapperComponent;

	@Test
	public void mapCSVToObject() {
		FileDetails fd = new FileDetails("SourceData.csv", "abc", 400000L, "dfgcbdveftdjkkshh",
				GenericConstants.statusPending, null, null, null, null, null, null, null, null, null, null, null);
		assertNotNull(dataMapperComponent.mapCSVToObject(fd, "1,512,840,22/02/2019 20:43:02,1.00"));
	}

	@Test
	public void mapTransactionsSummary() {
		TransactionSummaryDto dto = new TransactionSummaryDto(512, 200.000);
		FileDetails fd = new FileDetails("SourceData.csv", "abc", 400000L, "dfgcbdveftdjkkshh",
				GenericConstants.statusPending, null, null, null, null, null, null, null, null, null, null, null);
		assertNotNull(dataMapperComponent.mapTransactionsSummary(dto, fd));
	}

	@Test
	public void mapValidTransactions() {
		FileDetails fd = new FileDetails("SourceData.csv", "abc", 400000L, "dfgcbdveftdjkkshh",
				GenericConstants.statusPending, null, null, null, null, null, null, null, null, null, null, null);
		ValidTransactions vt = new ValidTransactions(1, 1, 512, 840, new Date(), 200.000, fd);
		assertNotNull(dataMapperComponent.mapInValidTransactions(vt));
	}

	@Test
	public void mapInValidTransactions() {
		FileDetails fd = new FileDetails("SourceData.csv", "abc", 400000L, "dfgcbdveftdjkkshh",
				GenericConstants.statusPending, null, null, null, null, null, null, null, null, null, null, null);
		InvalidTransactions ivt = new InvalidTransactions(1, "1,512,840,22/02/2019 20:43:02,1.00", fd);
		assertNotNull(dataMapperComponent.mapInValidTransactions(ivt));
	}

	@Test
	public void mapValidTransactionsQuery() {
		FileDetails fd = new FileDetails("SourceData.csv", "abc", 400000L, "dfgcbdveftdjkkshh",
				GenericConstants.statusPending, null, null, null, null, null, null, null, null, null, null, null);
		ValidTransactions vt = new ValidTransactions(1, 1, 512, 840, new Date(), 200.000, fd);
		assertNotNull(dataMapperComponent.mapValidTransactionsQuery(vt));
	}

	@Test
	public void mapInValidTransactionsQuery() {
		FileDetails fd = new FileDetails("SourceData.csv", "abc", 400000L, "dfgcbdveftdjkkshh",
				GenericConstants.statusPending, null, null, null, null, null, null, null, null, null, null, null);
		InvalidTransactions ivt = new InvalidTransactions(1, "1,512,840,22/02/2019 20:43:02,1.00", fd);
		assertNotNull(dataMapperComponent.mapInValidTransactionsQuery(ivt));
	}

}
