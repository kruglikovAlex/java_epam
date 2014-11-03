package com.epam.brest.courses.rest.controller;
import com.epam.brest.courses.domain.User;
import com.epam.brest.courses.rest.UserRestController;
import com.epam.brest.courses.rest.VersionRestController;
import com.epam.brest.courses.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
/**
 * Created by mentee-42 on 3.11.14.
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
        expect(userService.getUsers()).
                andReturn(UserDataFixture.getSampleUserList());
        replay(userService);
    }
    @Test
    public void getUsersTest() throws Exception {
        this.mockMvc.perform(
                get("/users")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"userId\":1,\"login\":\"login1\",\"userName\":\"name1\"}," +
                        "{\"userId\":2,\"login\":\"login2\",\"userName\":\"name2\"}," +
                        "{\"userId\":3,\"login\":\"login3\",\"userName\":\"name3\"}]"));
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
        public static User getExistUser(long id) {
            User user = new User();
            user.setUserId(id);
            user.setUserName("name");
            user.setLogin("login");
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