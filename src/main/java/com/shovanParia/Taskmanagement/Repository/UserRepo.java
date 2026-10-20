package com.shovanParia.Taskmanagement.Repository;

import com.shovanParia.Taskmanagement.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;


@Component
public interface UserRepo extends MongoRepository<User, ObjectId> {
    public User findByUserName(String userName);
}
