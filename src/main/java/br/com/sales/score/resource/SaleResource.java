package br.com.sales.score.resource;

import br.com.sales.score.dto.SaleDto;
import br.com.sales.score.service.SaleService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;

@Path(SaleResource.SALE_ENDPOINT_PREFIX)
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class SaleResource {

    public static final String SALE_ENDPOINT_PREFIX = "/sale";
    public static final String SALESMAN_FOR_HIGH_SALES_ENDPOINT = "/getSalesmanForHigherSales";
    public static final String TOP_SELLING_PRODUCT_ENDPOINT = "/getTopSellingProduct";

    @Inject
    private SaleService saleService;

    @POST
    public Response create(@Valid @NotNull SaleDto saleDto) {
        return Response.status(CREATED).entity(saleService.createSale(saleDto)).build();
    }

    @GET
    @Path(TOP_SELLING_PRODUCT_ENDPOINT)
    public Response getTopSellingProductDto() {
        return Response.status(OK).entity(saleService.getTopSellingProductDto()).build();
    }

    @GET
    @Path(SALESMAN_FOR_HIGH_SALES_ENDPOINT)
    public Response getSalesmanForHigherSalesDto() {
        return Response.status(OK).entity(saleService.getSalesmanForHigherSalesDto()).build();
    }
}
