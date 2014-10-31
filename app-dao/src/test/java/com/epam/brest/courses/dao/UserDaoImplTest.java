package com.epam.brest.courses.dao;

import com.epam.brest.courses.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})

public class UserDaoImplTest {
    User user;
    List<User> users;
    int sizeBefore = 0;

    @Autowired
    private UserDao userDao;

    @Before
    public void setUp() throws Exception {
        user = new User();
        users = userDao.getUsers();
    }

    @Test
    public void testGetUsers() {
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    public void testAddUser(){
        sizeBefore = users.size();
        //user.setUserId(null);
        user.setLogin(("userLogin3"));
        user.setUserName("userName3");

        userDao.addUser(user);

        users = userDao.getUsers();
        assertEquals(sizeBefore,users.size()-1);
    }

    @Test
    public void testRemoveUser() {
        assertNotNull(users);
        assertFalse(users.isEmpty());
        sizeBefore = users.size();

        userDao.removeUser(users.get((int)(Math.random()*sizeBefore)).getUserId());

        users = userDao.getUsers();
        assertEquals(sizeBefore-1,users.size());
    }

    @Test
    public void testRemoveAllUser(){
        sizeBefore = users.size();
        String testLogin = "in2";
        int countDel = 0;
        for (int i=0; i<sizeBefore; i++){
            if (users.get(i).getLogin().indexOf(testLogin)!=-1) countDel++;
        }

        userDao.removeAllUser(testLogin);

        users = userDao.getUsers();

        assertTrue(countDel!=0);
        assertTrue(users.size() < sizeBefore);
        assertTrue((users.size()+countDel) == sizeBefore);
    }

    @Test
    public void testGetUserByLogin(){
        sizeBefore = users.size();
        int indexUser = (int)(Math.random()*sizeBefore);

        String login = users.get(indexUser).getLogin();


        user.setUserId(users.get(indexUser).getUserId());
        user.setLogin(login);
        user.setUserName(users.get(indexUser).getUserName());

        assertEquals(userDao.getUserByLogin(login).toString(),user.toString());
    }

    @Test
    public void testGetUserById(){
        sizeBefore = users.size();
        int indexUser = (int)(Math.random()*sizeBefore);

        Long testid = users.get(indexUser).getUserId();

        user.setUserId(testid);
        user.setLogin(users.get(indexUser).getLogin());
        user.setUserName(users.get(indexUser).getUserName());

        assertEquals(userDao.getUserById(testid).toString(),user.toString());
    }

    @Test
    public void testGetUserByUserName(){
        sizeBefore = users.size();
        int indexUser = (int)(Math.random()*sizeBefore);

        String tesUserName = users.get(indexUser).getUserName();

        user.setUserId(users.get(indexUser).getUserId());
        user.setLogin(users.get(indexUser).getLogin());
        user.setUserName(tesUserName);

        assertEquals(userDao.getUserByUserName(tesUserName).toString(),user.toString());
    }

    @Test
    public void testUpdateUser(){
        sizeBefore = users.size();
        int indexUser = (int)(Math.random()*sizeBefore);

        Long testid = users.get(indexUser).getUserId();
        String testUser;

        user.setUserId(users.get(indexUser).getUserId());
        user.setLogin(users.get(indexUser).getLogin());
        user.setUserName(users.get(indexUser).getUserName());

        testUser = user.toString();

        user.setLogin("UpdateLogin");
        user.setUserName("UpdateUserName");

        userDao.updateUser(user);

        assertNotEquals(userDao.getUserById(testid).toString(),testUser);
        assertEquals(userDao.getUserById(testid).toString(),user.toString());
    }
}
