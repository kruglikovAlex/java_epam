package com.epam.brest.courses.client;


import com.epam.brest.courses.domain.User;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by mentee-42 on 12.11.14.
 */
public class RestClient {

    private static final Logger LOGGER = LogManager.getLogger();

    private String host;
    private RestTemplate restTemplate = new RestTemplate();

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestClient(String host) {
        this.host = host;
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(converters);

    }

    public String getRestVesrsion() {
        LOGGER.debug("getRestVesrsion {}", host);
        return restTemplate.getForObject(host + "/version", String.class);
    }

    public long addUser(User user) {
        LOGGER.debug("addUser {}", user);
        return restTemplate.postForObject(host+"/users", user, Long.class);
    }

    public User getUserById(Long id) {
        LOGGER.debug("getUserById {}", id);
        return restTemplate.getForObject(host+"/users/"+id, User.class);
    }

    public User getUserByLogin(String login) {
        LOGGER.debug("getUserByLogin {}", login);
        return restTemplate.getForObject(host+"/users/login/"+login, User.class);
    }

    public User[] getUsers() {
        LOGGER.debug("getUsers {}", host);
        return restTemplate.getForObject(host + "/users", User[].class);
    }

    public void updateUser(User user) {
        LOGGER.debug("updateUser {}", user);
        restTemplate.put(host + "/users", user);
    }

    public void removeUser(Long id) {
        LOGGER.debug("removeUser {}", id);
        restTemplate.delete(host + "/users/" + id);
    }
}
