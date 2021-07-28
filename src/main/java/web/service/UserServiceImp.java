package web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.RoleDAO;
import web.dao.UserDAO;
import web.model.Role;
import web.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {

    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserDAO userDAO, RoleDAO roleDAO, PasswordEncoder passwordEncoder){
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public UserServiceImp(){}

    @Override
    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return userDAO.listUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(int id) {
        return userDAO.getUser(id);
    }

    @Override
    @Transactional
    public void updateUser(int id, User user) {
        Set<Role> rol1 = new HashSet<>();
        for (Role role: user.getRoles()) {

            rol1.add(roleDAO.getRoleById(Integer.parseInt(role.getName())));
        }
        user.setRoles(rol1);
        user.setPassword(passwordEncoder.encode((user.getPassword())));
        userDAO.updateUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        userDAO.deleteUser(id);
    }

    @Override
    public Role showRole(int id) {
        Role role = roleDAO.getRoleById(id);

        return role;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        Set<Role> rol = new HashSet<>();
        for (Role role: user.getRoles()) {

            rol.add(roleDAO.getRoleById(Integer.parseInt(role.getName())));
        }
        user.setRoles(rol);
        user.setPassword(passwordEncoder.encode((user.getPassword())));
        userDAO.addUser(user);
    }

    @Override
    public User getUserByLogin(String name) {
        return findByUsername(name);
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    @Transactional
    public void addInitData() {
        if(userDAO.getUser(1)==null) {
            User admin = new User("ADMIN", 1,
                    1, passwordEncoder.encode("100"));
            Role role1 = new Role("ROLE_ADMIN");
            Role role2 = new Role("ROLE_USER");
            Set<Role> roles = new HashSet<>();
            roles.add(role1);
            roles.add(role2);
            admin.setRoles(roles);
            roleDAO.addRole(role1);
            roleDAO.addRole(role2);
            userDAO.addUser(admin);
        }
    }
}
