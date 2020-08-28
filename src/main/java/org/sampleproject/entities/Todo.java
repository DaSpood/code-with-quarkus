package org.sampleproject.entities;

import io.vertx.mutiny.sqlclient.Row;

import java.time.LocalDateTime;

public class Todo {
    public Long id;
    public String title;
    public String description;
    public LocalDateTime expireAt;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public LocalDateTime doneAt;
    public Integer version;

    public Todo() {}

    public Todo(Long id, String title,
                String desc,
                LocalDateTime expireAt,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                LocalDateTime doneAt,
                int version) {
        this.id = id;
        this.title = title;
        this.description = desc;
        this.expireAt = expireAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.doneAt = doneAt;
        this.version = version;
    }

    private static Todo from(Row row) {
        return new Todo(row.getLong("id"),
                row.getString("title"),
                row.getString("description"),
                row.getLocalDateTime("expireAt"),
                row.getLocalDateTime("createdAt"),
                row.getLocalDateTime("updatedAt"),
                row.getLocalDateTime("doneAt"),
                row.getInteger("version"));
    }
}
