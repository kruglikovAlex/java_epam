package com.epam.brest.courses.rest;

import com.epam.brest.courses.domain.User;
import com.epam.brest.courses.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Created by irina.
 */
@Controller
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity(user, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("User not found for id=" + id + " error:"
                    + ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/login/{login}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        User user = userService.getUserByLogin(login);
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<List<User>> getUsers() {
        List users = userService.getUsers();
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity<Long> addUser(@RequestBody User user) {
        Long id = userService.addUser(user);
        return new ResponseEntity(id, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity("", HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity removeUser(@PathVariable Long id) {
        userService.removeUser(id);
        return new ResponseEntity("", HttpStatus.OK);
    }
}