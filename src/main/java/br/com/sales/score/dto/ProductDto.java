package br.com.sales.score.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductDto {

    private Long id;

    @NotBlank
    private String sku;

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal price;

}
