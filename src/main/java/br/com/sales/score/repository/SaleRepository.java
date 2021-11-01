package br.com.sales.score.repository;

import br.com.sales.score.dto.SalesmanForHigherSalesDto;
import br.com.sales.score.dto.TopSellingProductDto;
import br.com.sales.score.entity.Sale;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SaleRepository implements PanacheRepositoryBase<Sale, UUID> {

    public List<SalesmanForHigherSalesDto> getSalesmanForHigherSales() {
        return getEntityManager().createNamedQuery("getSalesmanForHigherSalesDto").getResultList();
    }

    public List<TopSellingProductDto> getTopSellingProduct() {
        return getEntityManager().createNamedQuery("getTopSellingProducts").getResultList();
    }
}
