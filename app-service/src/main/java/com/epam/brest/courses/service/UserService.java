package com.epam.brest.courses.service;

import com.epam.brest.courses.domain.User;

import java.util.List;


public interface UserService {

    public Long addUser(User user);
    public User getUserByLogin(String login);
    public void removeUser(Long userId);
    public User getUserById(long userId);
    public void updateUser(User user);
    public List<User> getUsers();

}