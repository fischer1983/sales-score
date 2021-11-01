package br.com.sales.score.resource;

import br.com.sales.score.dto.SalesmanDto;
import br.com.sales.score.entity.Salesman;
import br.com.sales.score.repository.SalesmanRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static br.com.sales.score.resource.SalesmanResource.SALESMAN_ENDPOINT_PATH_ID;
import static br.com.sales.score.resource.SalesmanResource.SALESMAN_ENDPOINT_PREFIX;
import static br.com.sales.score.service.SalesmanService.SALESMAN_ALREADY_EXISTS;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.core.Is.is;

@QuarkusTest
class SalesmanResourceTest {

    @Inject
    SalesmanRepository salesmanRepository;

    @Test
    @DisplayName("Create a new Salesman")
    void shouldCreateSalesman() {
        var request = SalesmanDto.builder().registry("1234").name("John Doo").build();

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().post(SALESMAN_ENDPOINT_PREFIX)
                .then()
                .statusCode(201)
                .body("name", is(request.getName()),
                        "registry", is(request.getRegistry()));
    }

    @Test
    @DisplayName("Avoid to create a Salesman when registry already exists in another Salesman")
    void shouldValidateWhenRegistryAlreadyExistsInCreate() {

        var request = SalesmanDto.builder().registry("1234").name("John Doo").build();

        saveSalesman("1234", "John Doo");

        given()
                .body(request)
                .header("Content-Type", APPLICATION_JSON)
                .when().post(SALESMAN_ENDPOINT_PREFIX)
                .then()
                .statusCode(412)
                .body(is(SALESMAN_ALREADY_EXISTS+ " registry: " + request.getRegistry()));
    }

    @Test
    @DisplayName("Delete a Salesman")
    void shouldDeleteSalesman() {

        var salesman = saveSalesman("1234", "John Doo");

        given()
                .header("Content-Type", APPLICATION_JSON)
                .when().delete(SALESMAN_ENDPOINT_PREFIX + SALESMAN_ENDPOINT_PATH_ID, salesman.getId())
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Update a Salesman")
    void shouldUpdateSalesman() {

        var salesman = saveSalesman("1234", "John Doo");

        var salesmanDto = SalesmanDto.builder().registry("1234").name("Rick Doo").build();

        given()
                .body(salesmanDto)
                .header("Content-Type", APPLICATION_JSON)
                .when().put(SALESMAN_ENDPOINT_PREFIX + SALESMAN_ENDPOINT_PATH_ID, salesman.getId())
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Find a Salesman by registry")
    void shouldFindSalesmanByRegistry() {

        var salesman = saveSalesman("1234", "John Doo");

        given()
                 .header("Content-Type", APPLICATION_JSON)
                .when().get(SALESMAN_ENDPOINT_PREFIX + "?registry=" + salesman.getRegistry())
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Return not found in get when Salesman not exists")
    void shouldReturnNotFoundWhenFindSalesmanByRegistry() {

        given()
                .header("Content-Type", APPLICATION_JSON)
                .when().get(SALESMAN_ENDPOINT_PREFIX + "?registry=9999")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Return precondition failed in update when registry exists in another Salesman")
    void shouldValidateWhenRegistryAlreadyExistsInUpdate() {

        saveSalesman("1234", "John Doo");

        var salesman = saveSalesman("9999", "John Doo");
        var salesmanDto = SalesmanDto.builder().registry("1234").name("Rick Doo").build();

        given()
                .body(salesmanDto)
                .header("Content-Type", APPLICATION_JSON)
                .when().put(SALESMAN_ENDPOINT_PREFIX + SALESMAN_ENDPOINT_PATH_ID, salesman.getId())
                .then()
                .statusCode(412)
                .body(is(SALESMAN_ALREADY_EXISTS + " registry: 1234"));;
    }

    @Test
    @DisplayName("Return not found in delete when Salesman not exists")
    void shouldValidateWhenRegistryNotExistsInDelete() {

        given()
                .header("Content-Type", APPLICATION_JSON)
                .when().delete(SALESMAN_ENDPOINT_PREFIX + SALESMAN_ENDPOINT_PATH_ID,99L)
                .then()
                .statusCode(404);
    }

    @Transactional
    Salesman saveSalesman(String registry, String name) {
        salesmanRepository.persist(Salesman.builder().registry(registry).name(name).build());
        return salesmanRepository.findByRegistry(registry);
    }

    @BeforeEach
    @Transactional
    void beforeEach() {
        salesmanRepository.deleteAll();
    }
}