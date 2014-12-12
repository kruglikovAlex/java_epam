package com.epam.brest.courses.client;

import com.epam.brest.courses.domain.User;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class RestClientTest {
    static private final String HOST = "http://host";
    private RestClient client;
    private MockRestServiceServer mockServer;

    public String userForTestToString(User user){
        return "User{"+
                "userid="+user.getUserId()+
                ", login='"+user.getLogin()+'\''+
                ", username='"+user.getUserName()+'\''+
                '}';
    }

    @Before
    public void setUp(){
        client = new RestClient(HOST);
        mockServer = MockRestServiceServer.createServer(client.getRestTemplate());

    }

    @After
    public void check() {
        mockServer.verify();
    }

    @Test
    public void versionTest(){
        mockServer.expect(requestTo(HOST + "/version"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(withSuccess("123", MediaType.APPLICATION_JSON));

        String version = client.getRestVesrsion();
        assertEquals("123",version);
    }

    @Test
    public void addUserTest(){
        mockServer.expect(requestTo(HOST+"/users"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("{\"userId\":null,\"login\":\"login1\",\"userName\":\"name1\"}"))
                .andRespond(withSuccess("4",MediaType.APPLICATION_JSON));

        User user = new User();
        user.setLogin("login1");
        user.setUserName("name1");
        long id = client.addUser(user);
        assertEquals(4, id);
    }

    @Test
    public void getUserById(){
        mockServer.expect(requestTo(HOST+"/users/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"userId\":1,\"login\":\"login1\",\"userName\":\"name1\"}",MediaType.APPLICATION_JSON));

        User user = client.getUserById(1L);
        assertNotNull(user);
        assertNotNull(user.getUserId());
        assertEquals(new Long(1),user.getUserId());
        assertNotNull(user.getUserName());
        assertEquals("name1",user.getUserName());
        assertNotNull(user.getLogin());
        assertEquals("login1",user.getLogin());
    }

    @Test
    public void getUserByLogin(){
        mockServer.expect(requestTo(HOST+"/users/login/login1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"userId\":1,\"login\":\"login1\",\"userName\":\"name1\"}",MediaType.APPLICATION_JSON));

        User user = client.getUserByLogin("login1");
        assertNotNull(user);
        assertNotNull(user.getUserId());
        assertEquals(new Long(1),user.getUserId());
        assertNotNull(user.getUserName());
        assertEquals("name1",user.getUserName());
        assertNotNull(user.getLogin());
        assertEquals("login1",user.getLogin());
    }

    @Test
    public void getUsers() {
        mockServer.expect(requestTo(HOST + "/users"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"userId\":1,\"login\":\"login1\",\"userName\":\"name1\"}," +
                        "{\"userId\":2,\"login\":\"login2\",\"userName\":\"name2\"}]", MediaType.APPLICATION_JSON));
        User[] users = client.getUsers();
        assertEquals(2, users.length);
        assertNotNull(users[0]);
        assertNotNull(users[1]);
        assertNotNull(users[0].getUserId());
        assertNotNull(users[1].getUserId());
        assertEquals(new Long(1), users[0].getUserId());
        assertEquals(new Long(2), users[1].getUserId());
        assertNotNull(users[0].getLogin());
        assertNotNull(users[1].getLogin());
        assertEquals("login1", users[0].getLogin());
        assertEquals("login2", users[1].getLogin());
        assertNotNull(users[0].getUserName());
        assertNotNull(users[1].getUserName());
        assertEquals("name1", users[0].getUserName());
        assertEquals("name2", users[1].getUserName());
        assertEquals("User{userid=1, login='login1', username='name1'}", users[0].toString());
        assertEquals("User{userid=2, login='login2', username='name2'}", users[1].toString());
    }

    @Test
    public void updateUser() {
        mockServer.expect(requestTo(HOST+"/users"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("{\"userId\":1,\"login\":\"newlogin\",\"userName\":\"newname\"}"))
                .andRespond(withSuccess("",MediaType.APPLICATION_JSON));
        User user = new User();
        user.setUserId(1L);
        user.setLogin("newlogin");
        user.setUserName("newname");
        client.updateUser(user);
    }

    @Test
    public void removeUser() {
        mockServer.expect(requestTo(HOST + "/users/1"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));
        client.removeUser(1L);
    }
}

