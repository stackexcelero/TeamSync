package com.stackexcelero.dataAccess.utility;

import java.util.List;

public interface CrudDao<T> {
	T findById(int id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(int id);
}
