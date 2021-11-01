package br.com.sales.score.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class SalesmanForHigherSalesDto {
    private String salesmanRegistry;
    private String salesmanName;
    private BigDecimal totalSold;
}
