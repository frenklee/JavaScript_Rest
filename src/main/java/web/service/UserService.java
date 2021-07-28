package web.service;

import web.model.Role;
import web.model.User;

import java.util.List;

public interface UserService{
    public List<User> listUsers();
    public User getUser(int id);
    public void updateUser(int id, User user);
    public void deleteUser(int id);
    public void addUser(User user);
    public User getUserByLogin(String name);
    public void addInitData();
    public Role showRole(int id);
}
