package com.stackexcelero.dataAccess.dao;

import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.stackexcelero.dataAccess.model.Task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class TaskDAOImpl implements TaskDAO{
	private final Provider<EntityManager> emProvider;

    @Inject
    public TaskDAOImpl(Provider<EntityManager> emProvider) {
        this.emProvider = emProvider;
    }
    
    private EntityManager createEntityManager() {
        return emProvider.get();
    }

    @Override
    public Optional<Task> findById(int id) {
        EntityManager em = createEntityManager();
        return Optional.ofNullable(em.find(Task.class, id));
    }

    @Override
    public List<Task> findAll() {
        EntityManager em = createEntityManager();
        TypedQuery<Task> query = em.createQuery("SELECT u FROM task u", Task.class);
        return query.getResultList();
    }

    @Override
    public void save(Task entity) {
    	EntityManager em = createEntityManager();
    	if (entity.getTaskId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    @Override
    public void update(Task entity) {
        EntityManager em = createEntityManager();
        em.merge(entity);
    }

    @Override
    public void delete(int id) {
        EntityManager em = createEntityManager();
    	Task user = em.find(Task.class, id);
        if (user != null) {
            em.remove(user);
        }
    }
}
