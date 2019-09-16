package com.org.bchio.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.org.bchio.model.InvalidTransactions;

@Repository
public interface InvalidTransactionRepository extends CrudRepository<InvalidTransactions, Long> {

}
