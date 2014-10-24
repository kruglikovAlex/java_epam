package com.epam.brest.courses.domain;

import com.epam.brest.courses.domain.User;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Before;
import org.junit.Test;
import junit.textui.TestRunner;

public class UserTest extends TestCase {
    User user;
    public UserTest(String testName){
        super(testName);
    }

    @Before
    public void setUp() throws Exception {
        user = new User();
    }

    @Test
    public void testGetLogin() throws Exception {
        user.setLogin("User Login");
        assertEquals("User Login", user.getLogin());
    }

    @Test
    public void testGetUserName() throws Exception {
        user.setUserName("User Name");
        assertEquals("User Name", user.getUserName());
    }

    @Test
    public void testGetUserId() throws Exception {
        long idUser = (long)(Math.random()*100000);
        user.setUserId(idUser);
        assertEquals(idUser, user.getUserId());
    }

   /* @Test
    public void testAll() throws Exception {
        TestRunner runner = new TestRunner();
        TestSuite suite = new TestSuite();
        suite.addTest(new UserTest("testGetLogin"));
        suite.addTest(new UserTest("testGetUserName"));
        suite.addTest(new UserTest("testGetUserId"));
        runner.doRun(suite);
    }*/
}