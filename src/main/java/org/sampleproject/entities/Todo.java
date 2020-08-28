package org.sampleproject.entities;

import io.vertx.mutiny.sqlclient.Row;

public class Todo {
    public Long id;
    public String title;
    public String description;
    public Date expireAt;
    public Date createdAt;
    public Date updatedAt;
    public Date doneAt;
    public int version;

    public Todo() {}

    public Todo(Long id, String title, String desc,
                Date expireAt, Date createdAt, Date updatedAt, Date doneAt,
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
        return new Fruit(row.getLong("id"),
                row.getString("title"),
                row.getString("description"),
                row.getDate("expireAt"),
                row.getDate("createdAt"),
                row.getDate("updatedAt"),
                row.getDate("doneAt"),
                row.getInt("version"))
    }
}
