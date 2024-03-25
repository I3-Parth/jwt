package com.parth.jwt.controller;

import com.parth.jwt.model.UserEntity;
import com.parth.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<UserEntity> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/students")
    public String getAllStudents(){
        return "Students logged in successfully!";
    }

    @GetMapping("/teachers")
    public String getAllTeachers(){
        return "Teachers logged in successfully!";
    }

    @GetMapping("/office-admins")
    public String getAllOfficeAdmins(){
        return "Office admins logged in successfully!";
    }
}
