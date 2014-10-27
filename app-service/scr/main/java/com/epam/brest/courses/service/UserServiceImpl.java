package com.epam.brest.courses.service;

import com.epam.brest.courses.dao.UserDao;

import com.epam.brest.courses.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.Assert;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private static final Logger LOGGER = LogManager.getLogger();

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void addUser(User user) {
        Assert.notNull(user);
        Assert.isNull(user.getUserId());
        Assert.notNull(user.getLogin(), "User login should be specified.");
        Assert.notNull(user.getUserName(), "User name should be specified.");
        User existingUser = getUserByLogin(user.getLogin());
        if (existingUser != null) {
            throw new IllegalArgumentException("User is present in DB");
        }
        LOGGER.debug("addUser({})", user);
        userDao.addUser(user);
    }


    @Override
    public User getUserByLogin(String login) {
        User user = null;
        try {
            user = userDao.getUserByLogin(login);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.debug("EmptyResultDataAccessException");
        }
        return user;
    }
    @Override
    public void removeUser(long user_Id) {
        User user = userDao.getUserById(user_Id);
        Assert.isTrue(!user.getLogin().equals("login"));
        LOGGER.debug("removeUser(userId={}) ", user_Id);
        userDao.removeUser(user_Id);
    }
    @Override
    public User getUserById(long userId) {
        LOGGER.debug("getUserById(userId={}) ", userId);
        return userDao.getUserById(userId);
    }
    @Override
    public void updateUser(User user) {
        User oldUser = userDao.getUserById(user.getUserId());
        if(oldUser.getLogin().equals("login")) {
            Assert.isTrue(oldUser.getLogin().equals(user.getLogin()));
            Assert.isTrue(oldUser.getUserId()==(user.getUserId()));
        }
        LOGGER.debug("updateUser(user={}) ", user);
        userDao.updateUser(user);
    }
    @Override
    public List<User> getUsers() {
        LOGGER.debug("getUsers()");
        return userDao.getUsers();
    }
}
