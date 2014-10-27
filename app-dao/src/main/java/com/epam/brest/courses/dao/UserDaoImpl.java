package com.epam.brest.courses.dao;

import com.epam.brest.courses.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


public class UserDaoImpl implements UserDao {

    public static final String ADD_NEW_USER_SQL = "insert into USER (userid, login, username) VALUES (:userid, :login, :username)";

    public static final String DELETE_USER_SQL = "delete from USER where userid = ?";
    public static final String DELETE_ALL_USER_LIKE_LOGIN_SQL = "delete from USER where LCASE(login) like '%";

    public static final String UPDATE_USER_SQL = "update USER set username=:username, login=:login where userid=:userid";

    public static final String SELECT_USER_BY_LOGIN_SQL="select userid, login, username from USER where LCASE(login)=?";
    public static final String SELECT_USER_BY_ID_SQL="select userid, login, username from USER where userid=?";
    public static final String SELECT_USER_BY_NAME_SQL="select userid, login, username from USER where username=?";
    public static final String SELECT_ALL_USERS_SQL="select userid, login, username from USER";

    public static final String USER_ID = "userid";
    public static final String LOGIN = "login";
    public static final String USERNAME = "username";

    private static final Logger LOGGER = LogManager.getLogger();

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void addUser(User user) {
        LOGGER.debug("addUser({})", user);
        Map<String,Object> parameters = new HashMap(3);
        parameters.put(USER_ID, user.getUserId());
        parameters.put(LOGIN, user.getLogin());
        parameters.put(USERNAME, user.getUserName());
        namedJdbcTemplate.update(ADD_NEW_USER_SQL,parameters);
    }

    @Override
    public List<User> getUsers() {
        LOGGER.debug("getUsers()");
        return jdbcTemplate.query(SELECT_ALL_USERS_SQL, new UserMapper());
    }

    @Override
    public void removeUser(long userId) {
        LOGGER.debug("removeUser(userId={})",userId);
        jdbcTemplate.update(DELETE_USER_SQL, userId);
    }

    @Override
    public void removeAllUser(String login) {
        LOGGER.debug("removeAllUser(login={})",login);
        jdbcTemplate.update(DELETE_ALL_USER_LIKE_LOGIN_SQL+login.toLowerCase()+"%' ");//, new String[]{login.toLowerCase()});
    }

    @Override
    public User getUserByLogin(String login){
        LOGGER.debug("getUserByLogin(login={})",login);
        return jdbcTemplate.queryForObject(SELECT_USER_BY_LOGIN_SQL,new String[]{login.toLowerCase()}, new UserMapper());
    }

    @Override
    public User getUserById(long userId) {
        LOGGER.debug("getUserById(userId={})", userId);
        return jdbcTemplate.queryForObject(SELECT_USER_BY_ID_SQL, new UserMapper(),userId);
    }

    @Override
    public User getUserByUserName(String username){
        LOGGER.debug("getUserByUserName()", username);
        return jdbcTemplate.queryForObject(SELECT_USER_BY_NAME_SQL, new UserMapper(),username);
    }

    @Override
    public void updateUser(User user){
        LOGGER.debug("updateUser({})", user);
        Map<String, Object> parameters = new HashMap(3);
        parameters.put(USER_ID, user.getUserId());
        parameters.put(LOGIN,user.getLogin());
        parameters.put(USERNAME, user.getUserName());
        namedJdbcTemplate.update(UPDATE_USER_SQL,parameters);
    }

    public class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User();
            user.setUserId(rs.getLong(USER_ID));
            user.setLogin(rs.getString(LOGIN));
            user.setUserName(rs.getString(USERNAME));
            return user;
        }

    }
}
