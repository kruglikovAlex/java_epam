package com.epam.brest.courses.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

import static org.junit.Assert.assertEquals;

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
        user.setUserId(1234567);
        assertEquals(1234567, user.getUserId());
    }

    @Test
    public void testSetUserId() throws Exception {
        long IdUser = 11223344;
        user.setUserId(IdUser);
        assertEquals(IdUser,user.getUserId());
    }

}