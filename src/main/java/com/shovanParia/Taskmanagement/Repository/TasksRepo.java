package com.shovanParia.Taskmanagement.Repository;

import com.shovanParia.Taskmanagement.Entity.Tasks;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface TasksRepo extends MongoRepository<Tasks, ObjectId> {

}
