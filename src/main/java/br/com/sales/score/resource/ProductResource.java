package br.com.sales.score.resource;

import br.com.sales.score.dto.ProductDto;
import br.com.sales.score.service.ProductService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static br.com.sales.score.resource.ProductResource.PRODUCT_ENDPOINT_PREFIX;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;

@Path(PRODUCT_ENDPOINT_PREFIX)
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class ProductResource {

    public static final String PRODUCT_ENDPOINT_PREFIX = "/product";
    public static final String PRODUCT_ENDPOINT_PATH_ID = "/{id}";

    @Inject
    private ProductService productService;

    @POST
    public Response create(@Valid @NotNull ProductDto productDto) {
        return Response.status(CREATED)
                .entity(productService.create(productDto))
                .build();
    }

    @PUT
    @Path(PRODUCT_ENDPOINT_PATH_ID)
    public Response update(@PathParam("id") Long id, @Valid @NotNull ProductDto productDto) {
        productService.update(id, productDto);
        return Response.status(OK).build();
    }

    @DELETE
    @Path(PRODUCT_ENDPOINT_PATH_ID)
    public Response delete(@PathParam("id") Long id) {
        productService.delete(id);
        return Response.status(OK).build();
    }
}
