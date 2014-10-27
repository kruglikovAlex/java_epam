package com.epam.brest.courses.service;

import com.epam.brest.courses.domain.User;

import java.util.List;

/**
 * Created by mentee-42 on 24.10.14.
 */
public interface UserService {

    public void addUser(User user);
    public User getUserByLogin(String login);
    public void removeUser(long userId);
    public User getUserById(long userId);
    public void updateUser(User user);
    public List<User> getUsers();

}