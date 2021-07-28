package web.dao;

import org.springframework.stereotype.Repository;
import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RoleDAOImp implements RoleDAO{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Role> listRoles() {
        return em.createQuery("SELECT p FROM Role p", Role.class)
                .getResultList();
    }

    @Override
    public Role getRoleById(int id){ TypedQuery<Role> q = em.createQuery(
                "SELECT u FROM Role u WHERE u.id = :id", Role.class);
        q.setParameter("id", id);
        return q.getResultList().stream().findAny().orElse(null);
    }

    @Override
    public void addRole(Role role){
        em.persist(role);
    }
}
