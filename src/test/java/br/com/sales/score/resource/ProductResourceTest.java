package br.com.sales.score.resource;

import br.com.sales.score.dto.ProductDto;
import br.com.sales.score.entity.Product;
import br.com.sales.score.repository.ProductRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;

import static br.com.sales.score.resource.ProductResource.PRODUCT_ENDPOINT_PATH_ID;
import static br.com.sales.score.resource.ProductResource.PRODUCT_ENDPOINT_PREFIX;
import static br.com.sales.score.service.ProductService.PRODUCT_ALREADY_EXISTS;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.core.Is.is;

@QuarkusTest
class ProductResourceTest {

    @Inject
    ProductRepository productRepository;

    @Test
    @DisplayName("Create a new Product")
    void shouldCreateProduct() {
        var request = ProductDto.builder().sku("1234").name("iPhone 12").price(BigDecimal.valueOf(1.99)).build();

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().post(PRODUCT_ENDPOINT_PREFIX)
                .then()
                .statusCode(201)
                .body("name", is(request.getName()),
                        "sku", is(request.getSku()), "price",
                        is(request.getPrice().floatValue()));
    }

    @Test
    @DisplayName("Avoid to create a Product when sku already exists in another Product")
    void shouldValidateWhenSkuAlreadyExistsInCreate() {

        var request = ProductDto.builder().sku("1234").name("iPhone 12").price(BigDecimal.valueOf(1.99)).build();

        saveProduct("1234", "iPhone 12", BigDecimal.valueOf(1.99));

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().post(PRODUCT_ENDPOINT_PREFIX)
                .then()
                .statusCode(412)
                .body(is(PRODUCT_ALREADY_EXISTS + " sku: " + request.getSku()));
    }

    @Test
    @DisplayName("Update a Product")
    void shouldUpdateProduct() {

        var product = saveProduct("1234", "iPhone 12", BigDecimal.valueOf(1.99));

        var request = ProductDto.builder().sku("9876").name("iPhone 11").price(BigDecimal.valueOf(1.99)).build();

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().put(PRODUCT_ENDPOINT_PREFIX + PRODUCT_ENDPOINT_PATH_ID, product.getId())
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Return precondition failed in update when SKU exists in another product")
    void shouldValidateWhenRegistryAlreadyExistsInUpdate() {

        saveProduct("1234", "iPhone 12", BigDecimal.valueOf(1.99));

        var product = saveProduct("9876", "iPad 4", BigDecimal.valueOf(1.99));
        var request = ProductDto.builder().sku("1234").name("iPhone 11").price(BigDecimal.valueOf(1.99)).build();

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().put(PRODUCT_ENDPOINT_PREFIX + PRODUCT_ENDPOINT_PATH_ID, product.getId())
                .then()
                .statusCode(412)
                .body(is(PRODUCT_ALREADY_EXISTS + " sku: 1234"));
    }

    @Test
    @DisplayName("Delete a Product")
    void shouldDeleteProduct() {

        var product = saveProduct("1234", "iPhone 12", BigDecimal.valueOf(1.99));

        given()
                .header("Content-Type", APPLICATION_JSON)
                .when().delete(PRODUCT_ENDPOINT_PREFIX + PRODUCT_ENDPOINT_PATH_ID, product.getId())
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Return not found in delete when Product not exists")
    void shouldValidateWhenProductNotExistsInDelete() {

        given()
                .header("Content-Type", APPLICATION_JSON)
                .when().delete(PRODUCT_ENDPOINT_PREFIX + PRODUCT_ENDPOINT_PATH_ID, 99L)
                .then()
                .statusCode(404);
    }

    @Transactional
    Product saveProduct(String sku, String name, BigDecimal price) {
        productRepository.persist(Product.builder().sku(sku).name(name).price(price).build());
        return productRepository.findBySku(sku);
    }

    @BeforeEach
    @Transactional
    void beforeEach() {
        productRepository.deleteAll();
    }

}
