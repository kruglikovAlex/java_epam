package com.epam.brest.courses.rest;
import com.epam.brest.courses.domain.User;
import com.epam.brest.courses.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;
/**
 * Created by Berdahuk.
 */
@Controller
@RequestMapping("/users")
public class UserRestController {
    @Resource
    private UserService userService;
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @ResponseBody
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<List<User>> getUsers() {
        List users = userService.getUsers();
        return new ResponseEntity(users, HttpStatus.OK);
    }
}