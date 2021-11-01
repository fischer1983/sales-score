package br.com.sales.score.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
@Getter
public class TopSellingProductDto {
    private String productSku;
    private String productName;
    private BigInteger soldQuantity;
}
