package org.sampleproject.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.vertx.mutiny.sqlclient.Row;

import java.time.LocalDateTime;

public class Todo {
    public Integer id;
    public String title;
    public String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime expireAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime doneAt;
    public Integer version;

    public Todo() {}

    public Todo(Integer id,
                String title,
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

    public Todo(String title,String desc,LocalDateTime expireAt) {
        this.title = title;
        this.description = desc;
        this.expireAt = expireAt;
    }

    public static Todo from(Row row) {
        return new Todo(row.getInteger("id"),
                row.getString("title"),
                row.getString("description"),
                row.getLocalDateTime("expire_at"),
                row.getLocalDateTime("created_at"),
                row.getLocalDateTime("updated_at"),
                row.getLocalDateTime("done_at"),
                row.getInteger("version"));
    }
}
