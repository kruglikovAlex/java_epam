package com.epam.brest.courses.domain;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class UserTest extends TestCase {
    User user;

    @Before
    public void setUp() throws Exception {
        user = new User();
    }

    @Test
    public void testGetLogin() throws Exception {
        user.setLogin("User Login");
        assertEquals("User Login",user.getLogin());
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
}