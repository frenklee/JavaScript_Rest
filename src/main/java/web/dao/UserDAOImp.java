package web.dao;


import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDAOImp implements UserDAO{

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addUser(User user) {
        em.persist(user);
    }

    @Override
    public List<User> listUsers() {
        return em.createQuery("SELECT p FROM User p", User.class)
                .getResultList();
    }

    @Override
    public User getUser(int id) {
        TypedQuery<User> q = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
        q.setParameter("id", id);
        return q.getResultList().stream().findAny().orElse(null);
    }

    @Override
    public void updateUser(User user) {
        em.merge(user);
    }

    @Override
    public void deleteUser(int id) {
        em.remove(getUser(id));
    }

    public User findByUsername(String username){
        return em.createQuery("select u from User u where u.name = :name", User.class)
                .setParameter("name",username).getSingleResult();
    }
}
