package br.com.sales.score.service;

import br.com.sales.score.dto.SaleDto;
import br.com.sales.score.dto.SalesmanForHigherSalesDto;
import br.com.sales.score.dto.TopSellingProductDto;
import br.com.sales.score.entity.Product;
import br.com.sales.score.entity.Sale;
import br.com.sales.score.exception.ResourceNotFoundException;
import br.com.sales.score.repository.SaleRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static br.com.sales.score.service.ProductService.PRODUCT_NOT_FOUND;
import static br.com.sales.score.service.SalesmanService.SALESMAN_NOT_FOUND;

@RequestScoped
public class SaleService {

    @Inject
    private SaleRepository saleRepository;

    @Inject
    private SalesmanService salesmanService;

    @Inject
    private ProductService productService;

    @Transactional
    public SaleDto createSale(SaleDto saleDto) {

        var saleId = UUID.randomUUID();

        var salesman = salesmanService.findById(saleDto.getSalesmanId())
                .orElseThrow(() -> new ResourceNotFoundException(SALESMAN_NOT_FOUND + " id: " + saleDto.getSalesmanId()));

        var products = productService.findProductsById(saleDto.getProductsId());

        if (Objects.isNull(products) || products.size() == 0 || products.size() != saleDto.getProductsId().size()) {
            throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
        }

        var sale = Sale.builder()
                .id(saleId)
                .salesman(salesman)
                .products(products)
                .totalPrice(products.stream()
                        .map(Product::getPrice)
                        .reduce((totalPrice, price) -> price.add(totalPrice)).orElse(BigDecimal.ZERO).setScale(2))
                .build();

        saleRepository.persist(sale);

        sale = saleRepository.findById(saleId);

        return SaleDto.builder()
                .id(saleId.toString())
                .salesmanId(salesman.getId())
                .productsId(products.stream().map(Product::getId).collect(Collectors.toList()))
                .totalPrice(sale.getTotalPrice())
                .build();
    }

    public List<SalesmanForHigherSalesDto> getSalesmanForHigherSalesDto() {
        return saleRepository.getSalesmanForHigherSales();
    }

    public List<TopSellingProductDto> getTopSellingProductDto() {
        return saleRepository.getTopSellingProduct();
    }
}
