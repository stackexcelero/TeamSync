package com.stackexcelero.dataAccess.utility;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {
	Optional<T> getUserById(int id);

    List<T> getAll();

    void create(T entity);

    void update(T entity);

    void delete(int id);
}
