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

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    // For simplicity: all values must be provided or will be considered null (deleted).
    // Title field is still compulsory.
    // Using method PATCH instead of PUT because still just an update and not a full replacement.
    public Uni<Response> edit(@PathParam Integer id, Todo todo) {
        return repository.update(id, todo)
                .onItem().transform(modified -> modified == 1 ? Status.NO_CONTENT : (modified == 0 ? Status.NOT_FOUND : Status.BAD_REQUEST))
                .onItem().transform(status -> Response.status(status).build());
    }

    @PATCH
    @Path("{id}/expire")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> setExpiry(@PathParam Integer id, Todo todo) {
        return repository.updateExpireAt(id, todo.expireAt)
                .onItem().transform(modified -> modified ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }

    @PATCH
    @Path("{id}/done")
    // For simplicity: not forbidden to mark an already-done TODO done again.
    // The request will be accepted and the TODO's version and timestamp will be updated accordingly.
    public Uni<Response> markDone(@PathParam Integer id) {
        return repository.updateDoneAt(id)
                .onItem().transform(modified -> modified ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }

    @GET
    @Path("admin/")
    public String adminLogin() {
        return "{\"connection\" : \"success\"}";
    }

    @POST
    @Path("admin/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> post(Todo todo) {
        return repository.insert(todo)
                .onItem().transform(id -> id > 0 ? URI.create("/todo/" + id) : null)
                .onItem().transform(uri -> uri != null ? Response.created(uri).build() : Response.status(Status.BAD_REQUEST).build());
    }

    @DELETE
    @Path("admin/{id}")
    public Uni<Response> delete(@PathParam Integer id) {
        return repository.delete(id)
                .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }
}
