package com.epam.brest.courses.service;

import com.epam.brest.courses.dao.UserDao;
import com.epam.brest.courses.domain.User;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertNotNull;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
/**
 * Created by mentee-42 on 27.10.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-services-mock-test.xml"})
public class UserServiceImplMockTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @After
    public void clean() {
        reset(userDao);
    }

    @Test
    public void addUser() {
        User user = UserDataFixture.getNewUser();
        userDao.addUser(user);
        expectLastCall().andReturn(Long.valueOf(1L));
        userDao.getUserByLogin(user.getLogin());
        expectLastCall().andReturn(null);
        replay(userDao);
        Long id = userService.addUser(user);
        assertEquals(id, Long.valueOf(1L));
        // verify(userDao);
    }

    // @Test
    public void addUser2() {
        User user = UserDataFixture.getNewUser();
        userDao.addUser(user);
        expectLastCall().times(2);
        userDao.getUserByLogin(user.getLogin());
        expectLastCall().andReturn(null).times(2);
        replay(userDao);
        userService.addUser(user);
        userService.addUser(user);
        verify(userDao);
    }

    @Test
    public void getUserByLogin() {
        User user = UserDataFixture.getExistUser(1);
        expect(userDao.getUserByLogin(user.getLogin())).andReturn(user);
        replay(userDao);
        User result = userService.getUserByLogin(user.getLogin());
        verify(userDao);
        assertSame(user, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUserWithSameLogin() {
        User user = UserDataFixture.getNewUser();
        expect(userDao.getUserByLogin(user.getLogin())).andReturn(user);
        replay(userDao);
        userService.addUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void AddNotNullIdUser() {

        User user = UserDataFixture.getNotNullIdUser();

        userDao.getUserByLogin(user.getLogin());
        expectLastCall().andReturn(user);
        replay(userDao);

        userService.addUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void AddNullUser() {
        User user = UserDataFixture.getNullUser();

        userDao.getUserByLogin(user.getLogin());
        expectLastCall().andReturn(user);
        replay(userDao);

        userService.addUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void AddEmptyUser() {
        User user = UserDataFixture.getEmptyUser();

        userDao.getUserByLogin(user.getLogin());
        expectLastCall().andReturn(user);
        replay(userDao);

        userService.addUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void UpdateEmptyUser(){
        User user = UserDataFixture.getEmptyUser();

        userDao.getUserById(user.getUserId());
        expectLastCall().andReturn(user);
        replay(userDao);

        userService.updateUser(new User());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throwException() {
        User user = UserDataFixture.getNewUser();
        expect(userDao.getUserByLogin(user.getLogin())).andThrow(new UnsupportedOperationException());
        replay(userDao);
        userService.addUser(user);
    }
}