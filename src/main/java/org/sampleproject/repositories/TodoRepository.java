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
                .flatMap(r -> client.query("CREATE TABLE todos (id SERIAL PRIMARY KEY, title VARCHAR(100) NOT NULL, description TEXT, expireAt TIMESTAMP, createdAt TIMESTAMP NOT NULL, updatedAt TIMESTAMP NOT NULL, doneAt TIMESTAMP, version SMALLINT DEFAULT 1)").execute())
                .flatMap(r -> client.query("INSERT INTO todos (title, createdAt, updatedAt) VALUES ('First Entry', '2020-08-28 20:57:30+02', '2020-08-28 20:57:30+02')").execute())
                .flatMap(r -> client.query("INSERT INTO todos (title, description, createdAt, updatedAt) VALUES ('Second Entry', 'With a description!', '2020-08-28 21:00:30+02', '2020-08-28 21:00:30+02')").execute())
                .await().indefinitely();
    }

    public Multi<Todo> findAll() {
        return client.query("SELECT * FROM todos ORDER BY id ASC").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Todo::from);
    }

    public Uni<Todo> findById(int id) {
        return client.preparedQuery("SELECT * FROM todos WHERE id = $1").execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? Todo.from(iterator.next()) : null);
    }
}
