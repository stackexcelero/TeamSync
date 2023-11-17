package com.stackexcelero.dataAccess.service;

import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import com.stackexcelero.dataAccess.dao.AssignmentDAO;
import com.stackexcelero.dataAccess.dao.RoleDAO;
import com.stackexcelero.dataAccess.dao.TaskDAO;
import com.stackexcelero.dataAccess.dao.UserDAO;
import com.stackexcelero.dataAccess.model.Role;
import com.stackexcelero.dataAccess.model.User;

public class RoleServiceImpl implements RoleService{
	
	private final UserDAO userDAO;
	private final RoleDAO roleDAO;
	private final AssignmentDAO assignmentDAO;
	private final TaskDAO taskDAO;

    @Inject
    public RoleServiceImpl(UserDAO userDAO, RoleDAO roleDAO, AssignmentDAO assignmentDAO, TaskDAO taskDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.assignmentDAO = assignmentDAO;
        this.taskDAO = taskDAO;
    }

	@Override
	public List<Role> findAll() {
		return roleDAO.findAll();
	}

	@Override
	public void create(Role role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Role role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Role role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Role> find(Role inputRole) {
		return Optional.ofNullable(inputRole.getRoleId())
                .flatMap(roleDAO::findById)
                .or(() -> roleDAO.findByRoleName(inputRole.getRoleName()));
	}

}
