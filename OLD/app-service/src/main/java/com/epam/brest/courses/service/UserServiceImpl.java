package com.epam.brest.courses.service;

import com.epam.brest.courses.dao.UserDao;
import com.epam.brest.courses.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import org.springframework.util.Assert;


import java.util.List;

@Component
public class UserServiceImpl implements UserService {
    public static final String ERROR_DB_EMPTY = "There is no records in the database";
    public static final String ERROR_METHOD_PARAM = "The parameter can not be null";
    public static final String LOGIN = "ADMIN";
    public static final String ERROR_DELETE = "The user can not be deleted or changed";
    public static final String ERROR_USER = "In the database there is no user with such parameters";

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public Long addUser(User user) {
        Assert.notNull(user);
        Assert.isNull(user.getUserId());
        Assert.notNull(user.getLogin(), "User login should be specified.");
        Assert.notNull(user.getUserName(), "User name should be specified.");
        User existingUser = getUserByLogin(user.getLogin());
        if (existingUser != null) {
            throw new IllegalArgumentException("User is present in DB");
        }

        return userDao.addUser(user);
    }

    @Override
    public User getUserByLogin(String login) {
        LOGGER.debug("getUserByLogin({}) ", login);
        User user = null;//???????????????????????? testing without try
        try {
            user = userDao.getUserByLogin(login);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getUserByLogin({}) ", login);
        }
        return user;
    }

    @Override
    @Transactional
    public void removeUser(Long user_Id) {
        Assert.notNull(user_Id,ERROR_METHOD_PARAM);
        try {
            User user = userDao.getUserById(user_Id);
            if (user.getLogin().equals(LOGIN)) {
                throw new IllegalArgumentException(ERROR_DELETE);
            }
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn(ERROR_USER, user_Id);
            throw new IllegalArgumentException(ERROR_USER);
        }
        userDao.removeUser(user_Id);
    }

    @Override
    @Transactional
    public User getUserById(Long userId) {
        LOGGER.debug("getUserById(userId={}) ", userId);
        User user = null;
        try {
            user = userDao.getUserById(userId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("getUserById({}), Exception:{}",userId, e.toString());
        }
        return user;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        LOGGER.debug("updateUser(user={}) ", user);
        Assert.notNull(user);
        Assert.notNull(user.getUserId());
        Assert.notNull(user.getLogin(), "User login should be specified.");
        Assert.notNull(user.getUserName(), "User name should be specified.");
        if ((user.getLogin().equals(LOGIN))|(user.getUserName().equals(LOGIN)))
            throw new IllegalArgumentException(ERROR_DELETE);
        User existingUser = null;
        try {
            existingUser = userDao.getUserById(user.getUserId());
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(ERROR_USER);
        }
        if (existingUser != null) {
            userDao.updateUser(user);
        } else {
            LOGGER.warn(ERROR_USER);
            throw new IllegalArgumentException(ERROR_USER);
        }
    }

    @Override
    @Transactional
    public List<User> getUsers() {
        LOGGER.debug("getUsers()");
        // throw new NotImplementedException();
        List<User> users = userDao.getUsers();
        Assert.notEmpty(users, ERROR_DB_EMPTY);
        return users;
    }
}


