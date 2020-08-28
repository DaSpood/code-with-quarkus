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
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import java.net.URI;

@Path("todo/")
@Produces(MediaType.APPLICATION_JSON)
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> post(Todo todo) {
        return repository.insert(todo)
                .onItem().transform(id -> id > 0 ? URI.create("/todo/" + id) : null)
                .onItem().transform(uri -> uri != null ? Response.created(uri).build() : Response.status(Status.BAD_REQUEST).build());
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> edit() {
        return null; //TODO
    }

    @PATCH
    @Path("{id}/setexpire")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> addexpiry() {
        return null; //TODO
    }

    @PATCH
    @Path("{id}/done")
    public Uni<Response> markdone() {
        return null; //TODO
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@PathParam Integer id) {
        return repository.delete(id)
                .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }
}
