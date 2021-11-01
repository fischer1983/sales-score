package br.com.sales.score.repository;

import br.com.sales.score.entity.Salesman;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SalesmanRepository implements PanacheRepository<Salesman> {

    public Salesman findByRegistry(String registry) {
        return find("registry", registry).firstResult();
    }
}
