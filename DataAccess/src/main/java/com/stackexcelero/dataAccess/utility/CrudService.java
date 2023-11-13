package com.stackexcelero.dataAccess.utility;

import java.util.List;

public interface CrudService<T> {
	T getUserById(int userId);

    List<T> getAllUsers();

    void createUser(T user);

    void updateUser(T user);

    void deleteUser(int userId);
}
