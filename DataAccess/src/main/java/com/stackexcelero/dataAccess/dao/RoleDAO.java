package com.stackexcelero.dataAccess.dao;

import java.util.Optional;

import com.stackexcelero.dataAccess.model.Role;
import com.stackexcelero.dataAccess.utility.CrudDao;

public interface RoleDAO extends CrudDao<Role>{
	Optional<Role> findByRoleName(String roleName);
}
