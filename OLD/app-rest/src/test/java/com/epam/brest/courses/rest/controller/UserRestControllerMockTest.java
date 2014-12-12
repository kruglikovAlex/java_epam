package com.epam.brest.courses.rest.controller;

import com.epam.brest.courses.domain.User;
import com.epam.brest.courses.rest.UserRestController;
import com.epam.brest.courses.rest.VersionRestController;
import com.epam.brest.courses.rest.exception.NotFoundException;
import com.epam.brest.courses.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
/**
 * Created by irina on 3.11.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-rest-mock-test.xml"})
public class UserRestControllerMockTest {

    private MockMvc mockMvc;

    @Resource
    private UserRestController userRestController;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        this.mockMvc = standaloneSetup(userRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @After
    public void tearDown() throws Exception {
        reset(userService);
    }

    @Test
    public void getUserNotFoundTest() throws Exception {
        expect(userService.getUserById(5L)).andThrow(new NotFoundException("User not found for id=", "5"));
        replay(userService);

        this.mockMvc.perform(get("/users/5")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(userService);
    }

    @Test
    public void getUserByLoginRestTest() throws Exception {
        expect(userService.getUserByLogin("user2")).andReturn(UserDataFixture.getExistUser(2L));
        replay(userService);

        this.mockMvc.perform(get("/users/login/user2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"userId\":2,\"login\":\"login2\",\"userName\":\"name2\"}"));
        verify(userService);
    }

    @Test
    public void getUserByIdTest() throws Exception {
        expect(userService.getUserById(1L)).andReturn(UserDataFixture.getExistUser(1L));
        replay(userService);

        this.mockMvc.perform(
                get("/users/1")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"userId\":1,\"login\":\"login1\",\"userName\":\"name1\"}"));
        verify(userService);
    }

    @Test
    public void addUserTest() throws Exception {
        expect(userService.addUser(anyObject(User.class))).andReturn(Long.valueOf(1L));
        replay(userService);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(UserDataFixture.getNewUser());

        this.mockMvc.perform(
                post("/users")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
        verify(userService);
    }

    @Test
    public void getUsersTest() throws Exception {
        expect(userService.getUsers()).andReturn(UserDataFixture.getSampleUserList());
        replay(userService);

        this.mockMvc.perform(
                get("/users")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"userId\":1,\"login\":\"login1\",\"userName\":\"name1\"}," +
                        "{\"userId\":2,\"login\":\"login2\",\"userName\":\"name2\"}," +
                        "{\"userId\":3,\"login\":\"login3\",\"userName\":\"name3\"}]"));
        verify(userService);
    }

    @Test
    public void updateUserTest() throws Exception {
        userService.updateUser(anyObject(User.class));
        replay(userService);

        ObjectMapper objectMapper = new ObjectMapper();
        User user = UserDataFixture.getExistUser(1L);
        user.setUserName("modified");
        String userJson = objectMapper.writeValueAsString(user);

        ResultActions result = this.mockMvc.perform(
                put("/users")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());
        result.andExpect(status().isOk());
        verify(userService);
    }

    @Test
    public void deleteUserTest() throws Exception {

        userService.removeUser(1L);
        replay(userService);

        ResultActions result = this.mockMvc.perform(
                delete("/users/1"))
                .andDo(print());
        result.andExpect(status().isOk());
        verify(userService);
    }

    public static class UserDataFixture {

        public static User getNewUser() {
            User user = new User();
            user.setUserName("name");
            user.setLogin("login");
            return user;
        }
        public static User getNewUser(Long id) {
            User user = new User();
            user.setUserId(id);
            user.setUserName("name" + id);
            user.setLogin("login" + id);
            return user;
        }
        public static User getExistUser(Long id) {
            User user = new User();
            user.setUserId(id);
            user.setUserName("name" + id);
            user.setLogin("login" + id);
            return user;
        }
        public static List<User> getSampleUserList() {
            List list = new ArrayList(3);
            list.add(UserDataFixture.getNewUser(1L));
            list.add(UserDataFixture.getNewUser(2L));
            list.add(UserDataFixture.getNewUser(3L));
            return list;
        }
    }
}