package org.sampleproject.endpoints;

import org.sampleproject.entities.Todo;
import org.sampleproject.repositories.TodoRepository;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import java.net.URI;

@Path("todo/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoResource {
    private final TodoRepository repository;

    @Inject
    public TodoResource(TodoRepository repository) {
        this.repository = repository;
    }

    @GET
    public Multi<Todo> get() {
        return repository.findAll();
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingle(@PathParam Integer id) {
        return repository.findById(id)
                .onItem().transform(todo -> todo != null ? Response.ok(todo) : Response.status(Status.NOT_FOUND))
                .onItem().transform(ResponseBuilder::build);
    }
}
