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

}