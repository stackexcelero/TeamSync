package com.stackexcelero.dataAccess.dao;

import java.util.Optional;

import com.stackexcelero.dataAccess.model.User;
import com.stackexcelero.dataAccess.utility.CrudDao;

public interface UserDAO extends CrudDao<User> {
	Optional<User> findByUsername(String username);
}
