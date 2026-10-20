package com.shovanParia.Taskmanagement.Service;

import com.shovanParia.Taskmanagement.Entity.User;
import com.shovanParia.Taskmanagement.Repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
 private    UserRepo userRepo;
    @Autowired
  private   PasswordEncoder passwordEncoder;
    public   void save(User user){
        userRepo.save(user);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public User findByUserName(String userName){
        return  userRepo.findByUserName(userName);
    }
    public  Optional<User> findById(ObjectId id){
        return  userRepo.findById(id);
    }

    public void deleteByName(String userName) {

        User user = userRepo.findByUserName(userName);

        if (user != null) {
            userRepo.delete(user);
        }
    }

    public void saveNewUser(User user) {
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );
        userRepo.save(user);
    }
}
