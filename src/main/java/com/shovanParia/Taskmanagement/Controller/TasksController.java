package com.shovanParia.Taskmanagement.Controller;

import com.shovanParia.Taskmanagement.Entity.Tasks;
import com.shovanParia.Taskmanagement.Entity.User;
import com.shovanParia.Taskmanagement.Service.TaskService;
import com.shovanParia.Taskmanagement.Service.UserService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;


    // =========================
    // CREATE TASK
    // =========================
    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Tasks task) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String userName = authentication.getName();

        User user =
                userService.findByUserName(userName);


     //   System.out.println("TASK ID = " + task.getId().toHexString());

        if (user == null) {
            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        taskService.saveData(task, userName);
        System.out.println("TASK ID = " + task.getId().toHexString());
        return new ResponseEntity<>(
                task,
                HttpStatus.CREATED
        );
    }


    // =========================
    // GET MY TASKS
    // =========================
    @GetMapping
    public ResponseEntity<?> getByUserName() {
        // it take it from basic auth?
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String userName = authentication.getName();

        User user =
                userService.findByUserName(userName);

        if (user == null) {
            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                user.getTasksList(),
                HttpStatus.OK
        );
    }


    // =========================
    // DELETE MY TASK
    // =========================
    @DeleteMapping("/id/{taskId}")
    public ResponseEntity<?> deleteByIdUserName(
            @PathVariable ObjectId taskId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String userName = authentication.getName();

        User user =
                userService.findByUserName(userName);

        if (user == null) {
            return new ResponseEntity<>(
                    "User not found",
                    HttpStatus.NOT_FOUND
            );
        }

        try {

            taskService.delete(taskId, userName);

            return new ResponseEntity<>(
                    HttpStatus.NO_CONTENT
            );

        } catch (RuntimeException e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND
            );
        }
    }


    // =========================
    // UPDATE MY TASK
    // =========================
    @PutMapping("/id/{taskId}")
    public ResponseEntity<?> update(
            @RequestBody Tasks task,
            @PathVariable ObjectId taskId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String userName = authentication.getName();

        try {

            taskService.update(
                    task,
                    taskId,
                    userName
            );

            return new ResponseEntity<>(
                    task,
                    HttpStatus.OK
            );

        } catch (RuntimeException e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}