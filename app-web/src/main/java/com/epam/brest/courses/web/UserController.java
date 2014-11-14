package com.epam.brest.courses.web;

import com.epam.brest.courses.domain.User;
import com.epam.brest.courses.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by mentee-42 on 10.11.14.
 */
@Controller
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String init() {
        return "redirect:/usersList";
    }

    @RequestMapping("/inputForm")
    public ModelAndView launchInputForm() {
        return new ModelAndView("inputForm", "user", new User());
    }

    @RequestMapping("/submitData")
    public String getInputForm(@RequestParam("login")String login, @RequestParam("name")String userName) {
        User user = new User();
        user.setLogin(login);
        user.setUserName(userName);
        userService.addUser(user);
        return "redirect:/usersList";
    }

    @RequestMapping("/usersList")
    public ModelAndView getListUsersView() {
        List<User> users = userService.getUsers();
        LOGGER.debug("users.size = " + users.size());
        ModelAndView view = new ModelAndView("usersList", "users", users);
        return view;
    }

}
/*
@RequestMapping("/submitData2")
    public String deleteForm(@RequestParam("login")String login,@RequestParam("name")String userName){
        User user = userService.getUserByLogin(login);
        userService.removeUser(user.getUserId());
        return "redirect:/userListData";
    }
 */