package br.com.sales.score.entity;

import br.com.sales.score.dto.SalesmanForHigherSalesDto;
import br.com.sales.score.dto.TopSellingProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@NamedNativeQueries({@NamedNativeQuery(
        name = "getSalesmanForHigherSalesDto",
        query = "select\n" +
                "  sman.registry as salesmanRegistry,\n" +
                "  sman.\"name\" as salesmanName,\n" +
                "  sum(total_price) as totalSold\n" +
                "from sale s\n" +
                "join salesman sman on(s.salesman_id = sman.id)\n" +
                "group by sman.registry, sman.\"name\"\n" +
                "order by 3 desc",
        resultSetMapping = "SalesmanForHigherSalesDto"
),
        @NamedNativeQuery(
                name = "getTopSellingProducts",
                query = "select distinct p.sku as productSku, p.\"name\" as productName, count(1) as soldQuantity\n" +
                        "from sale_product sp\n" +
                        "join product p on (p.id = sp.product_id)\n" +
                        "group by p.sku, p.\"name\"\n" +
                        "order by 3 desc",
                resultSetMapping = "TopSellingProductDto"
        )
})
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "SalesmanForHigherSalesDto",
                classes = @ConstructorResult(
                        targetClass = SalesmanForHigherSalesDto.class,
                        columns = {
                                @ColumnResult(name = "salesmanRegistry"),
                                @ColumnResult(name = "salesmanName"),
                                @ColumnResult(name = "totalSold")
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "TopSellingProductDto",
                classes = @ConstructorResult(
                        targetClass = TopSellingProductDto.class,
                        columns = {
                                @ColumnResult(name = "productSku"),
                                @ColumnResult(name = "productName"),
                                @ColumnResult(name = "soldQuantity")
                        }
                )
        )
})
public class Sale {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name="salesman_id", foreignKey = @ForeignKey(name = "salesman_fk"))
    private Salesman salesman;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "sale_product",
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "product_fk")),
            joinColumns = @JoinColumn(name = "sale_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "sale_fk")))
    private List<Product> products;

    @Column(name = "total_price", scale = 2)
    private BigDecimal totalPrice;
}
