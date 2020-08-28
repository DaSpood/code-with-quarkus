package org.sampleproject.repositories;

import org.sampleproject.entities.Todo;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TodoRepository {
    private final PgPool client;

    @Inject
    @ConfigProperty(name = "sampleproject.schema.create", defaultValue = "true")
    boolean schemaCreate;

    @PostConstruct
    void config() {
        if (schemaCreate) {
            initdb();
        }
    }

    @Inject
    public TodoRepository(PgPool client) {
        this.client = client;
    }

    private void initdb() {
        client.query("DROP TABLE IF EXISTS todos").execute()
                .flatMap(r -> client.query("CREATE TABLE todos (id SERIAL PRIMARY KEY, title TEXT NOT NULL, description TEXT, expire_at TIMESTAMP, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, done_at TIMESTAMP, version SMALLINT NOT NULL DEFAULT 1)").execute())
                .await().indefinitely();
    }

    public Multi<Todo> findAll() {
        return client.query("SELECT * FROM todos ORDER BY id ASC").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Todo::from);
    }

    public Uni<Todo> findById(Integer id) {
        return client.preparedQuery("SELECT * FROM todos WHERE id = $1").execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? Todo.from(iterator.next()) : null);
    }

    public Uni<Integer> insert(Todo todo) {
        return client.preparedQuery("INSERT INTO todos (title, description, expire_at) VALUES ($1, $2, $3) RETURNING (id)").execute(Tuple.of(todo.title, todo.description, todo.expireAt))
                .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getInteger("id"));
    }

    public Uni<Boolean> delete(Integer id) {
        return client.preparedQuery("DELETE FROM todos WHERE id = $1").execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }
}
