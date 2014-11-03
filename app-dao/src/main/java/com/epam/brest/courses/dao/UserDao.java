package com.epam.brest.courses.dao;
import com.epam.brest.courses.domain.User;
import java.util.List;

public interface UserDao {
    public Long addUser(User user);
    public List<User> getUsers();
    public void removeUser(Long userId);
    public void removeAllUser(String login);
    public User getUserByLogin(String login);
    public User getUserById(Long userId);
    public User getUserByUserName(String userName);
    public void updateUser(User user);
}
