package com.epam.brest.courses.service;

import com.epam.brest.courses.dao.UserDao;
import com.epam.brest.courses.domain.User;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class UserServiceImpMockTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Test
    public void addUser() {
        User user = UserDataFixture.getNewUser();
        // ожидаемые действия mock
        userDao.getUserByLogin(user.getLogin());
        expectLastCall().andReturn(null);
        userDao.addUser(user);
        expectLastCall();
        //перевод mock в replay
        replay(userDao);

        userService.addUser(user);

        verify(userDao);
    }

    @Test
    public void addUser2(){
        User user = UserDataFixture.getNewUser();

        // ожидаемые действия mock
        userDao.getUserByLogin(user.getLogin());
        expectLastCall().andReturn(null);
        userDao.addUser(user);
        expectLastCall();
        userDao.getUserByLogin(user.getLogin());
        expectLastCall().andReturn(null);
        userDao.addUser(user);
        expectLastCall();

        replay(userDao);

        userService.addUser(user);
        userService.addUser(user);

        verify(userDao);
    }

    @Test
    public void getUserByLogin(){
        User user = UserDataFixture.getExistUser(1);
        userDao.getUserByLogin(user.getLogin());
        expectLastCall().andReturn(user);
        replay(userDao);
        User result = userService.getUserByLogin(user.getLogin());
        verify(userDao);
        assertSame(user,result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUserWithSameLogin(){
        User user = UserDataFixture.getNewUser();

        expect(userDao.getUserByLogin(user.getLogin())).andReturn(user);
        replay(userDao);

        userService.addUser(user);
        userService.addUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUserException(){
        User user = UserDataFixture.getNewUser();
        expect(userDao.getUserByLogin(user.getLogin())).andThrow(new NumberFormatException());
        replay(userDao);
    }
}
