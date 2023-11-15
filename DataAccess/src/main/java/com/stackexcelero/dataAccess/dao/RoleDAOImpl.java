package com.stackexcelero.dataAccess.dao;

import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.stackexcelero.dataAccess.model.Role;

import jakarta.persistence.EntityManager;
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
    public Optional<Role> findById(int id) {
        EntityManager em = createEntityManager();
        return Optional.ofNullable(em.find(Role.class, id));
    }
    
    @Override
    public Optional<Role> findByRoleName(String roleName) {
    	
    	if(roleName==null || roleName.isBlank())
    		return Optional.empty();
    	EntityManager em = createEntityManager();
        TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class);
        query.setParameter("roleName", roleName);
        List<Role> roles = query.getResultList();
        return roles.isEmpty() ? Optional.empty() : Optional.of(roles.get(0));
    }

    @Override
    public List<Role> findAll() {
        EntityManager em = createEntityManager();
        TypedQuery<Role> query = em.createQuery("SELECT u FROM role u", Role.class);
        return query.getResultList();
    }

    @Override
    public void save(Role entity) {
    	EntityManager em = createEntityManager();
    	if (entity.getRoleId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    @Override
    public void update(Role entity) {
        EntityManager em = createEntityManager();
        em.merge(entity);
    }

    @Override
    public void delete(int id) {
        EntityManager em = createEntityManager();
    	Role user = em.find(Role.class, id);
        if (user != null) {
            em.remove(user);
        }
    }
    
}
