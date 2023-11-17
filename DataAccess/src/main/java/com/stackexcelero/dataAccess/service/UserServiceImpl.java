package com.stackexcelero.dataAccess.service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.stackexcelero.dataAccess.dao.AssignmentDAO;
import com.stackexcelero.dataAccess.dao.RoleDAO;
import com.stackexcelero.dataAccess.dao.TaskDAO;
import com.stackexcelero.dataAccess.dao.UserDAO;
import com.stackexcelero.dataAccess.exception.InvalidUserInputException;
import com.stackexcelero.dataAccess.model.Assignment;
import com.stackexcelero.dataAccess.model.Role;
import com.stackexcelero.dataAccess.model.Task;
import com.stackexcelero.dataAccess.model.User;

public class UserServiceImpl implements UserService{
	
	
	private final RoleDAO roleDAO;
	private final UserDAO userDAO;
	private final AssignmentDAO assignmentDAO;
	private final TaskDAO taskDAO;

	private final RoleService roleService;
	private final AssignmentService AssignmentService;
	
    @Inject
    public UserServiceImpl(UserDAO userDAO, AssignmentDAO assignmentDAO, TaskDAO taskDAO,
    		RoleDAO roleDAO, RoleService roleService, AssignmentService AssignmentService) {
        this.userDAO = userDAO;
        this.assignmentDAO = assignmentDAO;
        this.taskDAO = taskDAO;
        this.roleDAO = roleDAO;
        this.roleService = roleService;
        this.AssignmentService = AssignmentService;
    }
	

	@Override
	public List<User> findAll() {
		return userDAO.findAll();
	}
	
	public void create(User inputUser) {
		if (inputUser == null) {
			throw new InvalidUserInputException();
	    }
		
		
		// Persist user with all associations
	    userDAO.save(inputUser);
	}
	
	@Override
	public Optional<User> find(User inputUser) {
		return Optional.ofNullable(inputUser.getUserId())
				.flatMap(userDAO::findById)
				.or(() -> userDAO.findByUsername(inputUser.getUsername()));
	}
	
	@Override
	public void update(User inputUser) {
		if (inputUser == null) {
	        throw new InvalidUserInputException();
	    }
		
		//finds user by id or by username. Or else throws exception
		User existingUser = find(inputUser).orElseThrow(() -> 
        new InvalidUserInputException("User not found with provided details - " + inputUser));

    	existingUser.setUsername(inputUser.getUsername());
    	existingUser.setPassword(inputUser.getPassword());
    	updateUserRoles(inputUser.getRoles(), existingUser);
    	
    	updateAssignments(inputUser.getAssignedAssignments(), existingUser);
    	updateAssignments(inputUser.getReceivedAssignments(), existingUser);
	}
	
	//TODO: Cascade operations in many-to-many relationships can lead to complex scenarios, 
	//      handling these operations manually in service layer might be more prudent.
	//      This way, i can precisely control what happens when adding or removing entities from relationships.
	private void updateUserRoles(Set<Role> inputRoles, User existingUser) {
		Set<Role> updatedRoles = new HashSet<>();
		for (Role userRole : inputRoles) {
			
			//find role by id or else by rolename
			Role existingRole = roleService.find(userRole).orElse(null);
			Role roleToUpdate;
			if(existingRole != null) {
				
				// If the role already exists, use the existing role
	            roleToUpdate = existingRole;
	            
	            if (!existingRole.getUsers().contains(existingUser)) {
	                existingRole.getUsers().add(existingUser);
	            }
			} else {
				
				// If it's a new role, create and save it
				roleToUpdate = createAndSaveNewRole(existingUser, userRole);
			}
			
			// Add the role to the updated roles set
	        updatedRoles.add(roleToUpdate);
		}
		// Update the user's roles
	    existingUser.setRoles(updatedRoles);
	}
	
	//Given a set of assignments, this method associates new one's and existing one's to usee's assignedAssignments set
	private void updateAssignments(Set<Assignment> inputAssignments, User user) {
		Set<Assignment> updatedAssignments = new HashSet<>();
		for(Assignment inputAssignment: inputAssignments) {
			
			//find assignment by id or else sets it null
			Assignment existingAssignment = AssignmentService.find(inputAssignment).orElse(null);
			
			Assignment assignmentToUpdate;
			if(existingAssignment != null) {
				assignmentToUpdate = existingAssignment;
				//TODO: Handle assignmentToUpdate's assignedAssignments and receivedAssignments
				//TODO: Handle assignmentToUpdate's set of tasks and it's properties
			} else {
				assignmentToUpdate = persistAssignment(inputAssignment);
			}
			
			updatedAssignments.add(assignmentToUpdate);
		}
		user.setAssignedAssignments(updatedAssignments);
	}
	
	//Method responsible for the creation of a new Assignment
	private Assignment persistAssignment(Assignment inputAssignment) {
		Assignment newAssignment = new Assignment();
		
		newAssignment.setAssignedDate(inputAssignment.getAssignedDate());
		newAssignment.setCompletedDate(inputAssignment.getCompletedDate());
		newAssignment.setInitialEstimation(inputAssignment.getInitialEstimation());
		newAssignment.setUpdatedEstimation(inputAssignment.getUpdatedEstimation());
		
		// Setting associated users
	    newAssignment.setAssignedBy(findOrCreateUser(inputAssignment.getAssignedBy()));
	    newAssignment.setAssignedTo(findOrCreateUser(inputAssignment.getAssignedTo()));
		
		return newAssignment;
	}
	
	private User findOrCreateUser(User inputUser) {
		if (inputUser == null) {
			throw new InvalidUserInputException();
		}

		// Check if the user already exists in the database
	    Optional<User> existingUser = find(inputUser);
	    User buffer;
	    
	    if(existingUser.isPresent()) {
	    	buffer = existingUser.get();
	    	System.out.println("Existing user fetched --> " + buffer);
	    	return buffer;
	    }else {
	    	buffer = persistUser(inputUser);
	    	System.out.println("Created new user --> " + buffer);
	    	return buffer;
	    }
	}
	private User persistUser(User user) {
	    User newUser = new User();
	    newUser.setUsername(user.getUsername());
	    newUser.setPassword(user.getPassword());
	    updateUserRoles(user.getRoles(), newUser);
	    userDAO.save(newUser);
	    return newUser;
	}
	
	private Role createAndSaveNewRole(User user, Role role) {
	    Role newRole = new Role();
	    newRole.setRoleName(role.getRoleName());
	    newRole.getUsers().add(user);
	    roleDAO.save(newRole);
	    return newRole;
	}

//	@Transactional(rollbackOn = Exception.class)
//	@Override
//	public void create(User inputUser) {
//	    if (inputUser == null) {
//	        return;
//	    }
//
//	    // Handling user creation or fetching
//	    User outputUser = handleUserCreation(inputUser);
//
//	    // Handling roles
//	    handleUserRoles(inputUser, outputUser);
//
//	    // Handling assigned assignments
//	    handleAssignedAssignments(inputUser, outputUser);
//
//	    // Handling received assignments
//	    handleReceivedAssignments(inputUser, outputUser);
//
//	    // Save the user entity after all associations are set
//	    userDAO.save(outputUser);
//	}
//	
//	private User handleUserCreation(User user) {
//	    return user.getUserId() != null
//	            ? userDAO.findById(user.getUserId()).orElseThrow(() -> new NullUserException())
//	            : createAndSaveNewUser(user);
//	}
//	
//	private User findOrCreateUser(User user) {
//		if (user == null) {
//	        throw new NullUserException();
//	    }
//
//		// Check if the user already exists in the database
//	    Optional<User> existingUser = userDAO.findByUsername(user.getUsername());
//	    User buffer;
//	    
//	    if(existingUser.isPresent()) {
//	    	buffer = existingUser.get();
//	    	System.out.println("Existing user fetched --> " + buffer);
//	    	return buffer;
//	    }else {
//	    	buffer = createAndSaveNewUser(user);
//	    	System.out.println("Created new user --> " + buffer);
//	    	return buffer;
//	    }
//	    //return existingUser.orElseGet(() -> createAndSaveNewUser(user));
//	}
//	
//	private User createAndSaveNewUser(User user) {
//	    User newUser = new User();
//	    newUser.setUsername(user.getUsername());
//	    newUser.setPassword(user.getPassword()); // Set additional properties as needed
//	    handleUserRoles(user, newUser);
//	    userDAO.save(newUser);
//	    return newUser;
//	}
//	
//	private void handleUserRoles(User inputUser, User outputUser) {
//		//For each Role 'r' of inputUser. Checks if 'r' exists on db, if not invokes createAndSaveNewRole(r)
//	    for (Role userRole : inputUser.getRoles()) {
//	        Role role = roleDAO.findByRoleName(userRole.getRoleName())
//	                           .orElseGet(() -> createAndSaveNewRole(userRole));
//	        outputUser.getRoles().add(role);
//	    }
//	}
//	
//	private Role createAndSaveNewRole(Role userRole) {
//	    Role newRole = new Role();
//	    newRole.setRoleName(userRole.getRoleName());
//	    roleDAO.save(newRole);
//	    return newRole;
//	}
//	
//	private void handleAssignedAssignments(User inputUser, User outputUser) {
//	    for (Assignment assignedAssignment : inputUser.getAssignedAssignments()) {
//	    	Assignment bufferAssignment;
//	        if (assignedAssignment.getAssignmentId() != null) {
//	            bufferAssignment = assignmentDAO.findById(assignedAssignment.getAssignmentId())
//	                                            .orElseGet(() -> createAssignment(assignedAssignment));
//	        } else {
//	            bufferAssignment = createAssignment(assignedAssignment);
//	        }
//
//	        // Setting associated users
//	        setAssignmentUsers(assignedAssignment, bufferAssignment);
//
//	        // Handle tasks within assignment
//	        handleTasks(assignedAssignment, bufferAssignment);
//
//	        outputUser.getAssignedAssignments().add(bufferAssignment);
//	        assignmentDAO.save(bufferAssignment); // Save assignment
//	    }
//	}
//	
//	private void handleReceivedAssignments(User user, User bufferUser) {
//	    for (Assignment receivedAssignment : user.getReceivedAssignments()) {
//	    	Assignment bufferAssignment;
//	        if (receivedAssignment.getAssignmentId() != null) {
//	            bufferAssignment = assignmentDAO.findById(receivedAssignment.getAssignmentId())
//	                                            .orElseGet(() -> createAssignment(receivedAssignment));
//	        } else {
//	            bufferAssignment = createAssignment(receivedAssignment);
//	        }
//
//	        // Setting associated users
//	        setAssignmentUsers(receivedAssignment, bufferAssignment);
//
//	        // Handle tasks within assignment
//	        handleTasks(receivedAssignment, bufferAssignment);
//
//	        bufferUser.getReceivedAssignments().add(bufferAssignment);
//	        assignmentDAO.save(bufferAssignment); // Save assignment
//	    }
//	}
//	
//	private void handleTasks(Assignment original, Assignment buffer) {
//	    for (Task task : original.getTasks()) {
//	        task.setAssignment(buffer);
//	        buffer.getTasks().add(task);
//	        taskDAO.save(task); // Save task explicitly
//	    }
//	}
//	
//	private Assignment createAssignment(Assignment assignment) {
//	    Assignment bufferAssignment = new Assignment();
//	    bufferAssignment.setAssignedDate(assignment.getAssignedDate());
//	    bufferAssignment.setCompletedDate(assignment.getCompletedDate());
//	    bufferAssignment.setInitialEstimation(assignment.getInitialEstimation());
//	    bufferAssignment.setUpdatedEstimation(assignment.getUpdatedEstimation());
//	    return bufferAssignment;
//	}
//	
//	private void setAssignmentUsers(Assignment original, Assignment buffer) {
//	    User assignedBy = findOrCreateUser(original.getAssignedBy());
//	    User assignedTo = findOrCreateUser(original.getAssignedTo());
//	    buffer.setAssignedBy(assignedBy);
//	    buffer.setAssignedTo(assignedTo);
//	}
	
	@Override
	public void delete(User inputUser) {
		userDAO.delete(inputUser.getUserId());
	}

}
