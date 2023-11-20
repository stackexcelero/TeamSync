package com.stackexcelero.dataAccess.service;

import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import com.stackexcelero.dataAccess.dao.AssignmentDAO;
import com.stackexcelero.dataAccess.dao.RoleDAO;
import com.stackexcelero.dataAccess.dao.TaskDAO;
import com.stackexcelero.dataAccess.dao.UserDAO;
import com.stackexcelero.dataAccess.model.Assignment;
import com.stackexcelero.dataAccess.model.Task;

public class TaskServiceImpl implements TaskService{
	
	private final RoleDAO roleDAO;
	private final UserDAO userDAO;
	private final AssignmentDAO assignmentDAO;
	private final TaskDAO taskDAO;

	
    @Inject
    public TaskServiceImpl(UserDAO userDAO, AssignmentDAO assignmentDAO, TaskDAO taskDAO,
    		RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.assignmentDAO = assignmentDAO;
        this.taskDAO = taskDAO;
        this.roleDAO = roleDAO;
    }
	
	@Override
	public Optional<Task> find(Task taskInput) {
		return Optional.ofNullable(taskInput.getTaskId())
				.flatMap(taskDAO::findById);
	}

	@Override
	public List<Task> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(Task taskInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Task taskInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Task taskInput) {
		// TODO Auto-generated method stub
		
	}
	
	//TODO: implement this
	@Override
	public void updateTaskAssignment(Task task, Assignment assignment) {}

}
