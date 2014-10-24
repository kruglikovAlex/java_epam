package com.epam.brest.courses.dao;
import com.epam.brest.courses.domain.User;
import java.util.List;

/**
 * Created by irina on 20.10.14.
 */
public interface UserDao {
    public void addUser(User user);
    public List<User> getUser();
    public void removeUser(long userId);

}
