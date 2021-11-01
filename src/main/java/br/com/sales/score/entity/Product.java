package br.com.sales.score.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "sku", name = "product_sku_uk"))
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private String sku;

    @Setter
    private String name;

    @Setter
    @Column(name = "price", scale = 2)
    private BigDecimal price;

}
