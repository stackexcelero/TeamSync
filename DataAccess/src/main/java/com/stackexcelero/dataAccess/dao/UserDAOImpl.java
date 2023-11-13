package com.stackexcelero.dataAccess.dao;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.stackexcelero.dataAccess.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
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
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findAll() {
        EntityManager em = createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(User entity) {
        EntityManager em = createEntityManager();
        try {
            em.persist(entity);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(User entity) {
        EntityManager em = createEntityManager();
        try {
            em.merge(entity);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager em = createEntityManager();
        try {
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
        } finally {
            em.close();
        }
    }

}
