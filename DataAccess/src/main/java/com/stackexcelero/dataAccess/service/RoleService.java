package com.stackexcelero.dataAccess.service;

import java.util.List;
import java.util.Optional;

import com.stackexcelero.dataAccess.model.Role;
import com.stackexcelero.dataAccess.model.User;

public interface RoleService {
	Optional<Role> find(Role roleInput);

    List<Role> findAll();

    void create(Role roleInput);

    void update(Role roleInput);

    void delete(Role roleInput);
    
    void associateUser(Role existingRole, User existingUser);
}
