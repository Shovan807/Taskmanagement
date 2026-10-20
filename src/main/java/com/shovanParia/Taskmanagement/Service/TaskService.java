package com.shovanParia.Taskmanagement.Service;

import com.shovanParia.Taskmanagement.Entity.Tasks;
import com.shovanParia.Taskmanagement.Entity.User;
import com.shovanParia.Taskmanagement.Repository.TasksRepo;
import com.shovanParia.Taskmanagement.Repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskService {
    @Autowired
    TasksRepo tasksRepo;
    @Autowired
    UserRepo userRepo;

    public void saveData(Tasks tasks, String UserName) {
        User user = userRepo.findByUserName(UserName);

        Tasks tasks1 = tasksRepo.save(tasks);    // save task in DB

        user.getTasksList().add(tasks1);        // add task to user's task list

        userRepo.save(user);                    // save updated user
    }

    public void delete(ObjectId id, String userName) {

        User user = userRepo.findByUserName(userName);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Remove task reference from user's task list
     boolean removed=   user.getTasksList().removeIf(x -> x.getId().equals(id));


        if(removed){
            // Save updated user
            userRepo.save(user);

            // Delete actual task from tasks collection
            tasksRepo.deleteById(id);
        }
    }

    //update
    public void update(Tasks tasks, ObjectId taskId, String userName) {

        User user = userRepo.findByUserName(userName);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Tasks oldTask = user.getTasksList()
                .stream()
                .filter(x -> x.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (tasks.getTitle() != null) {
            oldTask.setTitle(tasks.getTitle());
        }

        if (tasks.getDescription() != null) {
            oldTask.setDescription(tasks.getDescription());
        }

        if (tasks.getPriority() != null) {
            oldTask.setPriority(tasks.getPriority());
        }

        oldTask.setCompleted(tasks.isCompleted());

        tasksRepo.save(oldTask);

        userRepo.save(user);

    }

}
