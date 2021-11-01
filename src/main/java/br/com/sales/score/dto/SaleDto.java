package br.com.sales.score.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
public class SaleDto {

    private String id;

    @NotNull
    private Long salesmanId;

    @NotNull
    @Size(min = 1)
    private List<Long> productsId;

    private BigDecimal totalPrice;
}
