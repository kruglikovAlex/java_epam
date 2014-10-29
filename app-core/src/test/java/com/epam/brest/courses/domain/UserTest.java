package com.epam.brest.courses.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserTest {

    User user;

    /*public UserTest(String testName){
        super(testName);
    }*/

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
        Long idUser;
        idUser = new Long((long)(Math.random() * 10));
        user.setUserId(idUser);
        assertTrue(idUser== user.getUserId());
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