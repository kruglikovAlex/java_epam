package com.epam.brest.courses.service;

import com.epam.brest.courses.dao.UserDao;
import com.epam.brest.courses.domain.User;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.easymock.EasyMock.*;

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
}
