package br.com.sales.score.resource;

import br.com.sales.score.dto.SalesmanDto;
import br.com.sales.score.service.SalesmanService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static br.com.sales.score.resource.SalesmanResource.SALESMAN_ENDPOINT_PREFIX;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;


//TODO
//PACKAGE NAMES
//POSTMAN

//OPTIONAL
//OpenAPI Doc
@Path(SALESMAN_ENDPOINT_PREFIX)
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class SalesmanResource {

    public static final String SALESMAN_ENDPOINT_PREFIX = "/salesman";
    public static final String SALESMAN_ENDPOINT_PATH_ID = "/{id}";
    public static final String SALESMAN_ENDPOINT_ALL = "/all";

    @Inject
    private SalesmanService salesmanService;

    @POST
    public Response create(@Valid @NotNull SalesmanDto salesmanDto) {
        return Response.status(CREATED)
                .entity(salesmanService.create(salesmanDto))
                .build();
    }

    @PUT
    @Path(SALESMAN_ENDPOINT_PATH_ID)
    public Response update(@PathParam("id") Long id, @Valid @NotNull SalesmanDto salesmanDto) {
        salesmanService.update(id, salesmanDto);
        return Response.status(OK).build();
    }

    @GET
    public Response findByRegistry(@NotNull @QueryParam("registry") String registry) {
        return Response.ok(salesmanService.findByRegistry(registry)).build();
    }

    @GET
    @Path(SALESMAN_ENDPOINT_ALL)
    public Response findAll() {
        return Response.ok(salesmanService.findAll()).build();
    }

    @DELETE
    @Path(SALESMAN_ENDPOINT_PATH_ID)
    public Response delete(@PathParam("id") Long id) {
        salesmanService.delete(id);
        return Response.status(OK).build();
    }
}
