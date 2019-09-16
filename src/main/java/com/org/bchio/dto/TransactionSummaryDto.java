package com.org.bchio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionSummaryDto {

	private Integer orderingCurrency;
	private Double dealAmount;

}
