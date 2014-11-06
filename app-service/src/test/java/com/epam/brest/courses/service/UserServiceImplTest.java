package com.epam.brest.courses.service;

import com.epam.brest.courses.dao.UserDao;
import com.epam.brest.courses.domain.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-services-test.xml" })
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class UserServiceImplTest {
    public static final String ADMIN = "admin";
    //private UserDao userDao;

    int sizeBefore = 0;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullUser() throws Exception {
        userService.addUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEmptyUser() throws Exception {
        userService.addUser(new User());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNotNullIdUser() throws Exception {
        userService.addUser(new User(12L, "", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUserWithSameLogin() throws Exception {
        userService.addUser(new User(null, ADMIN, ADMIN));
        userService.addUser(new User(null, ADMIN, ADMIN));
    }

    @Test
    public void testAddUser() throws Exception {
        userService.addUser(new User(null, ADMIN, ADMIN));
        User user = userService.getUserByLogin(ADMIN);
        Assert.assertEquals(ADMIN, user.getLogin());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEmptyUser() throws Exception {
        userService.updateUser(new User());
     }

   // @Test
    public void testUpdateUser() throws Exception {
        List<User> users = userService.getUsers();
        sizeBefore = users.size();
        User user = userService.getUserById(1+(long)(Math.random()*sizeBefore));
        String testUser = user.toString();
        user.setLogin("updateLogin");
        user.setUserName("updateUserName");

        userService.updateUser(user);
        Long id_user = user.getUserId();

        Assert.assertNotEquals(testUser, userService.getUserById(id_user).toString());
        Assert.assertEquals(user.getLogin(),userService.getUserById(id_user).getLogin());
    }
}