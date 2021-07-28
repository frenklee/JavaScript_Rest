package web.dao;

import web.model.Role;
import web.model.User;

import java.util.List;

public interface RoleDAO {
    public Role getRoleById(int id);
    public void addRole(Role role);
    public List<Role> listRoles();
}
