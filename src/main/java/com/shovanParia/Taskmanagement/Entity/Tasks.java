package com.shovanParia.Taskmanagement.Entity;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("tasks")
public class Tasks {

    @Id
    private ObjectId id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    private LocalDateTime dueDate;
    private String priority;
    private boolean completed;
}