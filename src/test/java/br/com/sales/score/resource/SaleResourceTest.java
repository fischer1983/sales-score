package br.com.sales.score.resource;

import br.com.sales.score.dto.ProductDto;
import br.com.sales.score.dto.SaleDto;
import br.com.sales.score.dto.SalesmanDto;
import br.com.sales.score.repository.ProductRepository;
import br.com.sales.score.repository.SaleRepository;
import br.com.sales.score.repository.SalesmanRepository;
import br.com.sales.score.service.ProductService;
import br.com.sales.score.service.SalesmanService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static br.com.sales.score.resource.SaleResource.*;
import static br.com.sales.score.service.ProductService.PRODUCT_NOT_FOUND;
import static br.com.sales.score.service.SalesmanService.SALESMAN_NOT_FOUND;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;

@QuarkusTest
class SaleResourceTest {

    @Inject
    SalesmanService salesmanService;

    @Inject
    ProductService productService;

    @Inject
    SaleRepository saleRepository;

    @Inject
    SalesmanRepository salesmanRepository;

    @Inject
    ProductRepository productRepository;

    @Test
    @DisplayName("Create a new Sale")
    void shouldCreateSale() {

        var salesman = salesmanService.create(SalesmanDto.builder()
                .registry("1234")
                .name("Sale Man")
                .build());

        var product1 = productService.create(ProductDto.builder()
                .sku("9876")
                .name("Water 1L")
                .price(BigDecimal.valueOf(1.99))
                .build());

        var product2 = productService.create(ProductDto.builder()
                .sku("6543")
                .name("Ice Cream 1L")
                .price(BigDecimal.valueOf(5.99))
                .build());

        var request = SaleDto.builder()
                .salesmanId(salesman.getId())
                .productsId(List.of(product1.getId(), product2.getId()))
                .build();

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().post(SALE_ENDPOINT_PREFIX)
                .then()
                .statusCode(201)
                .body("salesmanId", is(request.getSalesmanId().intValue()),
                        "productsId", contains(product1.getId().intValue(), product2.getId().intValue()),
                        "totalPrice", is(7.98f));
    }

    @Test
    @DisplayName("Avoid to create a new Sale when is invalid Salesman")
    void shouldntCreateSaleWhenSalesmanIsInvalid() {

        var product = productService.create(ProductDto.builder()
                .sku("9876")
                .name("Water 1L")
                .price(BigDecimal.valueOf(1.99))
                .build());

        var request = SaleDto.builder()
                .salesmanId(99L)
                .productsId(List.of(product.getId()))
                .build();

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().post(SALE_ENDPOINT_PREFIX)
                .then()
                .statusCode(404)
                .body(is(SALESMAN_NOT_FOUND + " id: " + 99L));
    }

    @Test
    @DisplayName("Avoid to create a new Sale when is invalid Product")
    void shouldntCreateSaleWhenProductIsInvalid() {

        var salesman = salesmanService.create(SalesmanDto.builder()
                .registry("1234")
                .name("Sale Man")
                .build());

        var request = SaleDto.builder()
                .salesmanId(salesman.getId())
                .productsId(List.of(99L))
                .build();

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().post(SALE_ENDPOINT_PREFIX)
                .then()
                .statusCode(404)
                .body(is(PRODUCT_NOT_FOUND));
    }

    @Test
    @DisplayName("Return Salesman For Higher Sales")
    void shouldGetSalesmanForHigherSalesDto() {

        var salesman1 = salesmanService.create(SalesmanDto.builder()
                .registry("1234")
                .name("Sale Man 1234")
                .build());

        var salesman2 = salesmanService.create(SalesmanDto.builder()
                .registry("9876")
                .name("Sale Man 9876")
                .build());

        var product1 = productService.create(ProductDto.builder()
                .sku("9876")
                .name("Water 1L")
                .price(BigDecimal.valueOf(1.99))
                .build());

        var product2 = productService.create(ProductDto.builder()
                .sku("6543")
                .name("Ice Cream 1L")
                .price(BigDecimal.valueOf(5.99))
                .build());

        var request = SaleDto.builder()
                .salesmanId(salesman1.getId())
                .productsId(List.of(product1.getId()))
                .build();

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().post(SALE_ENDPOINT_PREFIX)
                .then()
                .statusCode(201);

        request = SaleDto.builder()
                .salesmanId(salesman2.getId())
                .productsId(List.of(product1.getId(), product2.getId()))
                .build();

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().post(SALE_ENDPOINT_PREFIX)
                .then()
                .statusCode(201);

        given()
                .header("Content-Type", APPLICATION_JSON)
                .when().get(SALE_ENDPOINT_PREFIX + SALESMAN_FOR_HIGH_SALES_ENDPOINT)
                .then()
                .statusCode(200)
                .body("totalSold", contains(7.98f, 1.99f));
    }

    @Test
    @DisplayName("Return Top Selling Products")
    void getTopSellingProductDto() {

        var salesman1 = salesmanService.create(SalesmanDto.builder()
                .registry("1234")
                .name("Sale Man 1234")
                .build());

        var salesman2 = salesmanService.create(SalesmanDto.builder()
                .registry("9876")
                .name("Sale Man 9876")
                .build());

        var product1 = productService.create(ProductDto.builder()
                .sku("9876")
                .name("Water 1L")
                .price(BigDecimal.valueOf(1.99))
                .build());

        var product2 = productService.create(ProductDto.builder()
                .sku("6543")
                .name("Ice Cream 1L")
                .price(BigDecimal.valueOf(5.99))
                .build());

        var request = SaleDto.builder()
                .salesmanId(salesman1.getId())
                .productsId(List.of(product1.getId()))
                .build();

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().post(SALE_ENDPOINT_PREFIX)
                .then()
                .statusCode(201);

        request = SaleDto.builder()
                .salesmanId(salesman2.getId())
                .productsId(List.of(product1.getId(), product2.getId()))
                .build();

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().post(SALE_ENDPOINT_PREFIX)
                .then()
                .statusCode(201);

        given()
                .header("Content-Type", APPLICATION_JSON)
                .when().get(SALE_ENDPOINT_PREFIX + TOP_SELLING_PRODUCT_ENDPOINT)
                .then()
                .statusCode(200)
                .body("productSku", contains(product1.getSku(), product2.getSku()),
                        "soldQuantity", contains(2, 1));
    }

    @BeforeEach
    @Transactional
    void beforeEach() {
        saleRepository.deleteAll();
        salesmanRepository.deleteAll();
        productRepository.deleteAll();
    }
}