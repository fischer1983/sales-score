package br.com.sales.score.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

public class ExceptionHandler {

    @Provider
    public static class ResourceAlreadyExistsExceptionHandled implements ExceptionMapper<ResourceAlreadyExistsException> {

        @Override
        public Response toResponse(ResourceAlreadyExistsException exception) {
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode())
                    .entity(exception.getMessage())
                    .build();
        }
    }

    @Provider
    public static class ResourceNotFoundExceptionHandled implements ExceptionMapper<ResourceNotFoundException> {

        @Override
        public Response toResponse(ResourceNotFoundException exception) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(exception.getMessage())
                    .build();
        }
    }
}
