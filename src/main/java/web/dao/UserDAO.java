package web.dao;

import web.model.User;

import java.util.List;

public interface UserDAO {
    public List<User> listUsers();
    public User getUser(int id);
    public void updateUser(User user);
    public void deleteUser(int id);
    public void addUser(User user);
    public User findByUsername(String username);
}
