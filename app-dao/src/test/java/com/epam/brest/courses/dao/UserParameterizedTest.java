package com.epam.brest.courses.dao;
import com.epam.brest.courses.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class UserParameterizedTest {
    public static final String ADMIN = "admin";

    @Autowired
    private UserDao userDao;

    private User user;

    public UserParameterizedTest(User user) {
        this.user = user;
    }

    @Before
    public void setUp() throws Exception {
        TestContextManager testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test() {
        userDao.addUser(user);
    }

    @Parameterized.Parameters
    public static Collection data() {
        Object[][] params = new Object[][] {
                {null},
                {new User()},
                {new User(12L, "", "")}
        };
        return Arrays.asList(params);
    }
}