package com.stackexcelero.dataAccess.service;

import java.io.IOException;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.stackexcelero.dataAccess.dao.AssignmentDAO;
import com.stackexcelero.dataAccess.dao.RoleDAO;
import com.stackexcelero.dataAccess.dao.TaskDAO;
import com.stackexcelero.dataAccess.dao.UserDAO;
import com.stackexcelero.dataAccess.model.User;

public class UserServiceImpl implements UserService{
	
	private final UserDAO userDAO;
	private final RoleDAO roleDAO;
	private final AssignmentDAO assignmentDAO;
	private final TaskDAO taskDAO;

    @Inject
    public UserServiceImpl(UserDAO userDAO, RoleDAO roleDAO, AssignmentDAO assignmentDAO, TaskDAO taskDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.assignmentDAO = assignmentDAO;
        this.taskDAO = taskDAO;
    }
	
	@Override
	public User getUserById(int userId) {
		return userDAO.findById(userId);
	}

	@Override
	public List<User> getAllUsers() {
		return userDAO.findAll();
	}
	
	@Transactional(rollbackOn = IOException.class)
	@Override
	public void createUser(User user) {
		
		
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(int userId) {
		userDAO.delete(userId);
	}

}
