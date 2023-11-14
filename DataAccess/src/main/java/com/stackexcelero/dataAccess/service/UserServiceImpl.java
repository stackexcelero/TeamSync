package com.stackexcelero.dataAccess.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.stackexcelero.dataAccess.dao.AssignmentDAO;
import com.stackexcelero.dataAccess.dao.RoleDAO;
import com.stackexcelero.dataAccess.dao.TaskDAO;
import com.stackexcelero.dataAccess.dao.UserDAO;
import com.stackexcelero.dataAccess.model.Assignment;
import com.stackexcelero.dataAccess.model.Role;
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
	public Optional<User> getUserById(int userId) {
		return userDAO.findById(userId);
	}

	@Override
	public List<User> getAllUsers() {
		return userDAO.findAll();
	}
	
	//The only method which given a user creates it's entities related entities too
	@Transactional(rollbackOn = IOException.class)
	@Override
	public void createUser(User user) {
		if (user == null) {
	        return;
	    }

	    User bufferUser = (user.getUserId()!=null) ? userDAO.findById(user.getUserId()).orElse(new User()): new User();
	    bufferUser.setUsername(user.getUsername());
	    bufferUser.setPassword(user.getPassword());

	    // Handling assigned assignments
	    user.getAssignedAssignments().stream()
	        .map(assigned -> {
	        	final Assignment bufferAssignment = (assigned.getAssignmentId() != null) ?  assignmentDAO.findById(assigned.getAssignmentId()).orElse(new Assignment()): new Assignment();
	        	// Ensure that assignedBy and assignedTo are managed entities
	            User managedAssignedBy = ensureUserIsManaged(assigned.getAssignedBy());
	            User managedAssignedTo = ensureUserIsManaged(assigned.getAssignedTo());
	            bufferAssignment.setAssignedBy(managedAssignedBy);
	            bufferAssignment.setAssignedTo(managedAssignedTo);
	            
	            bufferAssignment.setAssignedDate(assigned.getAssignedDate());
	            bufferAssignment.setCompletedDate(assigned.getCompletedDate());
	            bufferAssignment.setInitialEstimation(assigned.getInitialEstimation());
	            bufferAssignment.setUpdatedEstimation(assigned.getUpdatedEstimation());
	            
	            assigned.getTasks().forEach(task -> {
	            		task.setAssignment(bufferAssignment);
	                    bufferAssignment.getTasks().add(task);
	                    //taskDAO.save(task); //save parent first
	            });
	            return bufferAssignment;
	        })
	        .forEach(bufferAssignment -> {
	            bufferUser.getAssignedAssignments().add(bufferAssignment);
	            assignmentDAO.save(bufferAssignment);	//save parent first
	            bufferAssignment.getTasks().forEach(task -> {
	    	        taskDAO.save(task); // Saving the task explicitly
	    	    });
	        });
	    
	    // Handling received assignments
	    user.getReceivedAssignments().stream()
	    	.map(assignmentReceived -> {
	    		final Assignment bufferReceivedAssignment = (assignmentReceived.getAssignmentId() != null) ? assignmentDAO.findById(assignmentReceived.getAssignmentId()).orElse(new Assignment()): new Assignment();
	    		bufferReceivedAssignment.setAssignedBy(assignmentReceived.getAssignedBy());
	    		bufferReceivedAssignment.setAssignedTo(assignmentReceived.getAssignedTo());
	    		bufferReceivedAssignment.setAssignedDate(assignmentReceived.getAssignedDate());
	    		bufferReceivedAssignment.setCompletedDate(assignmentReceived.getCompletedDate());
	    		bufferReceivedAssignment.setInitialEstimation(assignmentReceived.getInitialEstimation());
	    		bufferReceivedAssignment.setUpdatedEstimation(assignmentReceived.getUpdatedEstimation());
	    		assignmentReceived.getTasks().forEach(task -> {
            		task.setAssignment(bufferReceivedAssignment);
            		bufferReceivedAssignment.getTasks().add(task);
	    		});
	    		return bufferReceivedAssignment;
	    	}).forEach(bufferReceivedAssignment -> {
	    		bufferUser.getReceivedAssignments().add(bufferReceivedAssignment);
	            assignmentDAO.save(bufferReceivedAssignment);	//save parent first
	            bufferReceivedAssignment.getTasks().forEach(task -> {
	    	        taskDAO.save(task); // Saving the task explicitly
	    	    });
	    	});;

	    // Handling roles
	    user.getRoles().stream()
	        .map(role -> {
	            final Role bufferRole = (role.getRoleId()!=null) ? roleDAO.findById(role.getRoleId()).orElse(new Role()): new Role();
	            bufferRole.setRoleName(role.getRoleName());
	            roleDAO.save(bufferRole);
	            return bufferRole;
	        })
	        .forEach(bufferRole -> {
	            bufferUser.getRoles().add(bufferRole);
	        });

	    userDAO.save(bufferUser);
		
	}
	private User ensureUserIsManaged(User user) {
	    if (user.getUserId() == null) {
	        userDAO.save(user);
	        return user;
	    } else {
	        return userDAO.findById(user.getUserId()).orElseThrow(() -> new IllegalStateException("User not found"));
	    }
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
