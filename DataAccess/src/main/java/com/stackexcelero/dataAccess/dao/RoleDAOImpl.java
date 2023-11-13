package com.stackexcelero.dataAccess.dao;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.stackexcelero.dataAccess.model.Role;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class RoleDAOImpl implements RoleDAO{
	
	private final Provider<EntityManager> emProvider;

    @Inject
    public RoleDAOImpl(Provider<EntityManager> emProvider) {
        this.emProvider = emProvider;
    }
    
    private EntityManager createEntityManager() {
        return emProvider.get();
    }

    @Override
    public Role findById(int id) {
        EntityManager em = createEntityManager();
        try {
            return em.find(Role.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Role> findAll() {
        EntityManager em = createEntityManager();
        try {
            TypedQuery<Role> query = em.createQuery("SELECT u FROM role u", Role.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Role entity) {
    	EntityManager em = createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin(); // begin the transaction
            em.persist(entity);
            transaction.commit(); // commit the transaction
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // rollback if an exception occurs
            }
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close(); // close the EntityManager only if it's open
            }
        }
    }

    @Override
    public void update(Role entity) {
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
        	Role user = em.find(Role.class, id);
            if (user != null) {
                em.remove(user);
            }
        } finally {
            em.close();
        }
    }
    
}
