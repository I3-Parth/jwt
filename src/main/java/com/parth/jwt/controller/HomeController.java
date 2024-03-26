package com.parth.jwt.controller;

import com.parth.jwt.model.UserEntity;
import com.parth.jwt.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Phaser;

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
    @PreAuthorize("hasRole('STUDENT')")
//    @RolesAllowed("STUDENT")
    public String getAllStudents(){
        return "Students logged in successfully!";
    }

    @GetMapping("/teachers")
    @PreAuthorize("hasRole('TEACHER')")
//    @RolesAllowed("TEACHER")
    public String getAllTeachers(){
        return "Teachers logged in successfully!";
    }

    @GetMapping("/office-admins")
    @PreAuthorize("hasRole('OFFICE_ADMIN')")
//    @RolesAllowed("OFFICE_ADMIN")
    public String getAllOfficeAdmins(){
        return "Office admins logged in successfully!";
    }
}
