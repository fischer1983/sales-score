package br.com.sales.score.repository;

import br.com.sales.score.entity.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    public Product findBySku(String sku) {
        return find("sku", sku).firstResult();
    }
}
