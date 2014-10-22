package com.epam.brest.courses.dao;

import com.epam.brest.courses.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static org.junit.Assert.*;

/**
 * Created by irina on 22.10.14.
 */
public class UserDaoImplTest {
    @Autowired
    private UserDao userDao;

    public void getUsers() {
        List<User> users = userDao.getUser();
        assertNotNull(users);

    }
}
