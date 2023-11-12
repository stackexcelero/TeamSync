package com.stackexcelero.dataAccess.dao;

import java.util.List;

public interface CrudOperations<T> {
	T findById(int id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(int id);
}
