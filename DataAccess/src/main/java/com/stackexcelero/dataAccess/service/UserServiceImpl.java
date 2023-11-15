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
import com.stackexcelero.dataAccess.exception.NullUserException;
import com.stackexcelero.dataAccess.model.Assignment;
import com.stackexcelero.dataAccess.model.Role;
import com.stackexcelero.dataAccess.model.Task;
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
	public List<User> getAll() {
		return userDAO.findAll();
	}
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public void create(User inputUser) {
	    if (inputUser == null) {
	        return;
	    }

	    // Handling user creation or fetching
	    User outputUser = handleUserCreation(inputUser);

	    // Handling roles
	    handleUserRoles(inputUser, outputUser);

	    // Handling assigned assignments
	    handleAssignedAssignments(inputUser, outputUser);

	    // Handling received assignments
	    handleReceivedAssignments(inputUser, outputUser);

	    // Save the user entity after all associations are set
	    userDAO.save(outputUser);
	}
	
	private User handleUserCreation(User user) {
	    return user.getUserId() != null
	            ? userDAO.findById(user.getUserId()).orElseThrow(() -> new NullUserException())
	            : createAndSaveNewUser(user);
	}
	
	private void handleUserRoles(User inputUser, User outputUser) {
		//For each Role 'r' of inputUser. Checks if 'r' exists on db, if not invokes createAndSaveNewRole(r)
	    for (Role userRole : inputUser.getRoles()) {
	        Role role = roleDAO.findByRoleName(userRole.getRoleName())
	                           .orElseGet(() -> createAndSaveNewRole(userRole));
	        outputUser.getRoles().add(role);
	    }
	}
	
	private Role createAndSaveNewRole(Role userRole) {
	    Role newRole = new Role();
	    newRole.setRoleName(userRole.getRoleName());
	    roleDAO.save(newRole);
	    return newRole;
	}
	
	private void handleAssignedAssignments(User inputUser, User outputUser) {
	    for (Assignment assignedAssignment : inputUser.getAssignedAssignments()) {
	    	Assignment bufferAssignment;
	        if (assignedAssignment.getAssignmentId() != null) {
	            bufferAssignment = assignmentDAO.findById(assignedAssignment.getAssignmentId())
	                                            .orElseGet(() -> createAssignment(assignedAssignment));
	        } else {
	            bufferAssignment = createAssignment(assignedAssignment);
	        }

	        // Setting associated users
	        setAssignmentUsers(assignedAssignment, bufferAssignment);

	        // Handle tasks within assignment
	        handleTasks(assignedAssignment, bufferAssignment);

	        outputUser.getAssignedAssignments().add(bufferAssignment);
	        assignmentDAO.save(bufferAssignment); // Save assignment
	    }
	}
	
	private void handleReceivedAssignments(User user, User bufferUser) {
	    for (Assignment receivedAssignment : user.getReceivedAssignments()) {
	    	Assignment bufferAssignment;
	        if (receivedAssignment.getAssignmentId() != null) {
	            bufferAssignment = assignmentDAO.findById(receivedAssignment.getAssignmentId())
	                                            .orElseGet(() -> createAssignment(receivedAssignment));
	        } else {
	            bufferAssignment = createAssignment(receivedAssignment);
	        }

	        // Setting associated users
	        setAssignmentUsers(receivedAssignment, bufferAssignment);

	        // Handle tasks within assignment
	        handleTasks(receivedAssignment, bufferAssignment);

	        bufferUser.getReceivedAssignments().add(bufferAssignment);
	        assignmentDAO.save(bufferAssignment); // Save assignment
	    }
	}
	
	private void handleTasks(Assignment original, Assignment buffer) {
	    for (Task task : original.getTasks()) {
	        task.setAssignment(buffer);
	        buffer.getTasks().add(task);
	        taskDAO.save(task); // Save task explicitly
	    }
	}
	
	private Assignment createAssignment(Assignment assignment) {
	    Assignment bufferAssignment = new Assignment();
	    bufferAssignment.setAssignedDate(assignment.getAssignedDate());
	    bufferAssignment.setCompletedDate(assignment.getCompletedDate());
	    bufferAssignment.setInitialEstimation(assignment.getInitialEstimation());
	    bufferAssignment.setUpdatedEstimation(assignment.getUpdatedEstimation());
	    return bufferAssignment;
	}
	
	private void setAssignmentUsers(Assignment original, Assignment buffer) {
	    User assignedBy = findOrCreateUser(original.getAssignedBy());
	    User assignedTo = findOrCreateUser(original.getAssignedTo());
	    buffer.setAssignedBy(assignedBy);
	    buffer.setAssignedTo(assignedTo);
	}
	
	private User findOrCreateUser(User user) {
		if (user == null) {
	        throw new NullUserException();
	    }

		// Check if the user already exists in the database
	    Optional<User> existingUser = userDAO.findByUsername(user.getUsername());
	    return existingUser.orElseGet(() -> createAndSaveNewUser(user));
	}
	
	private User createAndSaveNewUser(User user) {
	    User newUser = new User();
	    newUser.setUsername(user.getUsername());
	    newUser.setPassword(user.getPassword()); // Set additional properties as needed
	    userDAO.save(newUser);
	    return newUser;
	}
	
//	@Transactional(rollbackOn = IOException.class)
//	@Override
//	public void create(User user) {
//		if (user == null) {
//	        return;
//	    }
//
//	    User bufferUser = (user.getUserId()!=null) ? userDAO.findById(user.getUserId()).orElse(new User()): new User();
//	    bufferUser.setUsername(user.getUsername());
//	    bufferUser.setPassword(user.getPassword());
//	    
//	    // Handling roles
//    	user.getRoles().forEach(userRole -> {
//    	    Role role = roleDAO.findByRoleName(userRole.getRoleName())
//    	                       .orElseGet(() -> {
//    	                           Role newRole = new Role();
//    	                           newRole.setRoleName(userRole.getRoleName());
//    	                           roleDAO.save(newRole);
//    	                           return newRole;
//    	                       });
//    	    bufferUser.getRoles().add(role);
//    	});
//
//	    // Handling assigned assignments
//	    user.getAssignedAssignments().stream()
//	        .map(assignedAssignments -> {
//	        	final Assignment bufferAssignedAssignment = (assignedAssignments.getAssignmentId() != null) ?  assignmentDAO.findById(assignedAssignments.getAssignmentId()).orElse(new Assignment()): new Assignment();
//	        	
//	        	bufferAssignedAssignment.setAssignedDate(assignedAssignments.getAssignedDate());
//	            bufferAssignedAssignment.setCompletedDate(assignedAssignments.getCompletedDate());
//	            bufferAssignedAssignment.setInitialEstimation(assignedAssignments.getInitialEstimation());
//	            bufferAssignedAssignment.setUpdatedEstimation(assignedAssignments.getUpdatedEstimation());
//	            
//	            assignedAssignments.getTasks().forEach(task -> {
//	            		task.setAssignment(bufferAssignedAssignment);
//	            		bufferAssignedAssignment.getTasks().add(task);
//	            });
//	            return bufferAssignedAssignment;
//	        })
//	        .forEach(bufferAssignedAssignment -> {
//	        	User assignedBy = userDAO.findByUsername(bufferAssignedAssignment.getAssignedTo().getUsername()) 
//	        			.orElseGet( () -> {
//	        				// Ensure that assignedBy is managed
//                            User newUser = new User();
//                            newUser.setUsername(bufferAssignedAssignment.getAssignedBy().getUsername());
//                            newUser.setPassword(bufferAssignedAssignment.getAssignedBy().getPassword());
//                            newUser.getAssignedAssignments().add(bufferAssignedAssignment);
//                            userDAO.save(newUser);
//                            return newUser;
//	        			});
//	        	User assignedTo = userDAO.findByUsername(bufferAssignedAssignment.getAssignedBy().getUsername())
//	        			.orElseGet( () -> {
//	        				// Ensure that assignedTo is managed
//                            User newUser = new User();
//                            newUser.setUsername(bufferAssignedAssignment.getAssignedTo().getUsername());
//                            newUser.setPassword(bufferAssignedAssignment.getAssignedTo().getPassword());
//                            newUser.getReceivedAssignments().add(bufferAssignedAssignment);
//                            userDAO.save(newUser);
//                            return newUser;
//	        			});
//	        	bufferAssignedAssignment.setAssignedBy(assignedBy);
//	        	bufferAssignedAssignment.setAssignedTo(assignedTo);
//	        	
//	            bufferUser.getAssignedAssignments().add(bufferAssignedAssignment);
//	            assignmentDAO.save(bufferAssignedAssignment);	//save parent first
//	            bufferAssignedAssignment.getTasks().forEach(task -> {
//	    	        taskDAO.save(task); // Saving the task explicitly
//	    	    });
//	        });
//	    
//	    // Handling received assignments
//	    user.getReceivedAssignments().stream()
//	    	.map(assignmentReceived -> {
//	    		final Assignment bufferReceivedAssignment = (assignmentReceived.getAssignmentId() != null) ? assignmentDAO.findById(assignmentReceived.getAssignmentId()).orElse(new Assignment()): new Assignment();
//	    		
//	    		bufferReceivedAssignment.setAssignedDate(assignmentReceived.getAssignedDate());
//	    		bufferReceivedAssignment.setCompletedDate(assignmentReceived.getCompletedDate());
//	    		bufferReceivedAssignment.setInitialEstimation(assignmentReceived.getInitialEstimation());
//	    		bufferReceivedAssignment.setUpdatedEstimation(assignmentReceived.getUpdatedEstimation());
//	    		
//	    		assignmentReceived.getTasks().forEach(task -> {
//            		task.setAssignment(bufferReceivedAssignment);
//            		bufferReceivedAssignment.getTasks().add(task);
//	    		});
//	    		
//	    		return bufferReceivedAssignment;
//	    	}).forEach(bufferReceivedAssignment -> {
//	    		User assignedBy = userDAO.findByUsername(bufferReceivedAssignment.getAssignedBy().getUsername())
//                        .orElseGet(() -> {
//                        	// Ensure that assignedBy is managed
//                            User newUser = new User();
//                            newUser.setUsername(bufferReceivedAssignment.getAssignedBy().getUsername());
//                            newUser.setPassword(bufferReceivedAssignment.getAssignedBy().getPassword());
//                            newUser.getAssignedAssignments().add(bufferReceivedAssignment);
//                            userDAO.save(newUser);
//                            return newUser;
//                        });
//	    		
//	    		User assignedTo = userDAO.findByUsername(bufferReceivedAssignment.getAssignedTo().getUsername())
//	    				.orElseGet(() -> {
//	    					// Ensure that assignedTo is managed
//	    					User newUser = new User();
//	    					newUser.setUsername(bufferReceivedAssignment.getAssignedTo().getUsername());
//                            newUser.setPassword(bufferReceivedAssignment.getAssignedTo().getPassword());
//                            newUser.getReceivedAssignments().add(bufferReceivedAssignment);
//                            userDAO.save(newUser);
//                            return newUser;
//	    				});
//	    		bufferReceivedAssignment.setAssignedBy(assignedBy);
//	    		bufferReceivedAssignment.setAssignedTo(assignedTo);
//	    	    
//	    		bufferUser.getReceivedAssignments().add(bufferReceivedAssignment);
//	            assignmentDAO.save(bufferReceivedAssignment);	//save parent first
//	            bufferReceivedAssignment.getTasks().forEach(task -> {
//	    	        taskDAO.save(task); // Saving the task explicitly
//	    	    });
//	    	});;
//
//	    userDAO.save(bufferUser);	
//	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int userId) {
		userDAO.delete(userId);
	}

}
