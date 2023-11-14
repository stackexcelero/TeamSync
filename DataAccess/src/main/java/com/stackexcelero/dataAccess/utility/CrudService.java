package com.stackexcelero.dataAccess.utility;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {
	Optional<T> getUserById(int userId);

    List<T> getAllUsers();

    void createUser(T user);

    void updateUser(T user);

    void deleteUser(int userId);
}
