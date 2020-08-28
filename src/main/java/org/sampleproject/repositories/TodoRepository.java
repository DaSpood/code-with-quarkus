package org.sampleproject.repositories;

import org.sampleproject.entities.Todo;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.inject.Inject;

public class TodoRepository {
    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

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
