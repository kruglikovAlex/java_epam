package com.epam.brest.courses.dao;

import com.epam.brest.courses.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/testApplicationContextSpring.xml"})
public class UserDaoImplTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void addUser(){
        List<User> users = userDao.getUser();
        int sizeBefore = users.size();
        User user = new User();
        user.setUserId(3L);
        user.setLogin(("userLogin3"));
        user.setUserName("userName3");

        userDao.addUser(user);

        users = userDao.getUser();
        assertEquals(sizeBefore,users.size()-1);
    }

    @Test
    public void getUsers() {
        List<User> users = userDao.getUser();
        if (users.size() == 0) {
            User user = new User();
            user.setUserId(3L);
            user.setLogin(("userLogin3"));
            user.setUserName("userName3");

            userDao.addUser(user);

            users = userDao.getUser();

            assertNotNull(users);
            assertFalse(users.isEmpty());
        } else {
            assertNotNull(users);
            assertFalse(users.isEmpty());
        }

    }

    @Test
    public void removeUsers() {
        List<User> users = userDao.getUser();
        int sizeBefore = users.size();
        if (sizeBefore==0) {
            User user = new User();
            user.setUserId(3L);
            user.setLogin(("userLogin3"));
            user.setUserName("userName3");

            userDao.addUser(user);

            users = userDao.getUser();
            sizeBefore = users.size();
        }

        for (int i=0; i<sizeBefore; i++) {
            userDao.removeUser(users.get(i).getUserId());
        }

        users = userDao.getUser();
        assertTrue(users.isEmpty());
    }
}
