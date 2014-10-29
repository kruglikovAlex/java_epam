package com.epam.brest.courses.service;

import com.epam.brest.courses.dao.UserDao;
import com.epam.brest.courses.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.Assert;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger();

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void addUser(User user) {
        LOGGER.debug("addUser(user={}) ", user);
        Assert.notNull(user);
        Assert.notNull(user.getUserId());
        Assert.notNull(user.getLogin(), "User login should be specified.");
        Assert.notNull(user.getUserName(), "User name should be specified.");
        Assert.hasLength(user.getLogin(), "User login mustn't empty");
        Assert.hasLength(user.getUserName(), "User name mustn't empty");
        User existingUser = userDao.getUserByLogin(user.getLogin());
        if (existingUser != null) {
            LOGGER.error("getUserByLogin({}) ", user.getLogin());
            throw new IllegalArgumentException("User is present in DB");
        }
        userDao.addUser(user);
    }

    @Override
    public User getUserByLogin(String login) {
        LOGGER.debug("getUserByLogin({}) ", login);
        User user = null;
        try {
            user = userDao.getUserByLogin(login);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getUserByLogin({}) ", login);
        }
        return user;
    }

    @Override
    public void removeUser(Long user_Id) {
        User user = userDao.getUserById(user_Id);
        Assert.isTrue(!user.getLogin().equals("login"));
        LOGGER.debug("removeUser(userId={}) ", user_Id);
        userDao.removeUser(user_Id);
    }

    @Override
    public User getUserById(Long userId) {
        LOGGER.debug("getUserById(userId={}) ", userId);
        return userDao.getUserById(userId);
    }

    @Override
    public void updateUser(User user) {
        LOGGER.debug("updateUser(user={}) ", user);
        Assert.notNull(user);
        Assert.notNull(user.getUserId());
        Assert.notNull(user.getLogin(), "User login should be specified.");
        Assert.notNull(user.getUserName(), "User name should be specified.");
        User existingUser = userDao.getUserById(user.getUserId());
        if (existingUser != null) {
            userDao.updateUser(user);
        } else {
            LOGGER.error("getUserById({}) ", user.getUserId());
            throw new IllegalArgumentException("User don't present in DB");
        }


    }

    @Override
    public List<User> getUsers() {
        LOGGER.debug("getUsers()");
        return userDao.getUsers();
    }
}

