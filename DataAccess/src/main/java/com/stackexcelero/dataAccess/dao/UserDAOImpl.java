package com.stackexcelero.dataAccess.dao;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.stackexcelero.dataAccess.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class UserDAOImpl implements UserDAO{
	
	private final Provider<EntityManager> emProvider;

    @Inject
    public UserDAOImpl(Provider<EntityManager> emProvider) {
        this.emProvider = emProvider;
    }
    
    private EntityManager createEntityManager() {
        return emProvider.get();
    }

    @Override
    public User findById(int id) {
        EntityManager em = createEntityManager();
        return em.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        EntityManager em = createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM appuser u", User.class);
        return query.getResultList();
    }

    @Override
    public void save(User entity) {
    	EntityManager em = createEntityManager();
        em.persist(entity);
    }

    @Override
    public void update(User entity) {
        EntityManager em = createEntityManager();
        em.merge(entity);
    }

    @Override
    public void delete(int id) {
        EntityManager em = createEntityManager();
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
    }

}
