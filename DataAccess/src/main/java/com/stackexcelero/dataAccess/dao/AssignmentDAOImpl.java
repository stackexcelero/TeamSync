package com.stackexcelero.dataAccess.dao;

import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.stackexcelero.dataAccess.model.Assignment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class AssignmentDAOImpl implements AssignmentDAO{

	private final Provider<EntityManager> emProvider;

    @Inject
    public AssignmentDAOImpl(Provider<EntityManager> emProvider) {
        this.emProvider = emProvider;
    }
    
    private EntityManager createEntityManager() {
        return emProvider.get();
    }

    @Override
    public Optional<Assignment> findById(int id) {
        EntityManager em = createEntityManager();
        return Optional.ofNullable(em.find(Assignment.class, id));
    }

    @Override
    public List<Assignment> findAll() {
        EntityManager em = createEntityManager();
        TypedQuery<Assignment> query = em.createQuery("SELECT u FROM assignment u", Assignment.class);
        return query.getResultList();
    }

    @Override
    public void save(Assignment entity) {
    	EntityManager em = createEntityManager();
    	if (entity.getAssignmentId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    @Override
    public void update(Assignment entity) {
        EntityManager em = createEntityManager();
        em.merge(entity);
    }

    @Override
    public void delete(int id) {
        EntityManager em = createEntityManager();
    	Assignment user = em.find(Assignment.class, id);
        if (user != null) {
            em.remove(user);
        }
    }

}
