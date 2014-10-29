package com.epam.brest.courses.service;

import com.epam.brest.courses.domain.User;
import org.hsqldb.lib.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;


/**
 * Created by irina on 27.10.14.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"classpath*:/testServiceApplicationContextSpring.xml"})
public class UserParameterizedTest {

    @Autowired
    private UserService userService;
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
        userService.addUser(user);
    }

    public static Collection data() {
        Object[][] params = new Object[][] {
                {null},
                {new User()},
                {new User(12l,"","")}
        };
        return (Collection) Arrays.asList(params);
    }
}
