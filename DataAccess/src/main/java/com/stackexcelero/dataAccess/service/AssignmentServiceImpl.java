package com.stackexcelero.dataAccess.service;

import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import com.stackexcelero.dataAccess.dao.AssignmentDAO;
import com.stackexcelero.dataAccess.dao.RoleDAO;
import com.stackexcelero.dataAccess.dao.TaskDAO;
import com.stackexcelero.dataAccess.dao.UserDAO;
import com.stackexcelero.dataAccess.model.Assignment;
import com.stackexcelero.dataAccess.model.User;

public class AssignmentServiceImpl implements AssignmentService{
	
	private final RoleDAO roleDAO;
	private final UserDAO userDAO;
	private final AssignmentDAO assignmentDAO;
	private final TaskDAO taskDAO;

	
    @Inject
    public AssignmentServiceImpl(UserDAO userDAO, AssignmentDAO assignmentDAO, TaskDAO taskDAO,
    		RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.assignmentDAO = assignmentDAO;
        this.taskDAO = taskDAO;
        this.roleDAO = roleDAO;
    }

	@Override
	public Optional<Assignment> find(Assignment assignmentInput) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(assignmentInput.getAssignmentId())
				.flatMap(assignmentDAO::findById);
	}

	@Override
	public List<Assignment> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(Assignment assignmentInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Assignment assignmentInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Assignment assignmentInput) {
		// TODO Auto-generated method stub
		
	}
	
	//TODO: Implement this
	@Override
	public void updateExecutor(Assignment assignment, User executor) {}
	
	//TODO: Implement this
	@Override
	public void updateInitiator(Assignment assignment, User initiator) {}

}
