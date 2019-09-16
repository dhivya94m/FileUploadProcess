package com.org.bchio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.org.bchio.dto.TransactionSummaryDto;
import com.org.bchio.model.ValidTransactions;

@Repository
public interface ValidTransactionRepository extends CrudRepository<ValidTransactions, Long> {

	@Query("SELECT new com.org.bchio.dto.TransactionSummaryDto(v.orderingCurrency, SUM(v.dealAmount)) FROM ValidTransactions v WHERE v.fileDetails.fileName=?1 GROUP BY  v.orderingCurrency")
	List<TransactionSummaryDto> getSummaryData(String fileName);

}
