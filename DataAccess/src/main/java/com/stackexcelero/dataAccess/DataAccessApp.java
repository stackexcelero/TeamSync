package com.stackexcelero.dataAccess;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.stackexcelero.dataAccess.dao.UserDAO;
import com.stackexcelero.dataAccess.model.Assignment;
import com.stackexcelero.dataAccess.model.Role;
import com.stackexcelero.dataAccess.model.Task;
import com.stackexcelero.dataAccess.model.User;
import com.stackexcelero.dataAccess.utility.GuiceModule;

public class DataAccessApp {
	
	
	public final UserDAO userDAO;
	
	@Inject
	public DataAccessApp(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public static void main(String[] args) {
		System.out.println("----INITIALIZATION----");
		Injector injector = Guice.createInjector(new GuiceModule());
		try {
			DataAccessApp dataAccessApp = injector.getInstance(DataAccessApp.class);
			dataAccessApp.insertOneRecord();
            
            // Additional logic or application flow here...
        } catch (Exception e) {
            // Handle initialization errors
            e.printStackTrace();
        } finally {
            System.out.println("----TERMINATION----");
        }
	}

	private void insertOneRecord() {
		User employee = new User();
        User manager = new User();
        
        employee.setUsername("carl.kenneth@example.com");
        employee.setPassword("psw12345");
        manager.setUsername("richard.smith@example.com");
        manager.setPassword("psw54321");
        
        Role employeeRole = new Role();
        employeeRole.setRoleName("Employee");
        Role managerRole = new Role();
        managerRole.setRoleName("Manager");
        
        Assignment assignment = new Assignment();
        Task task = new Task();

        employee.setRoles(new HashSet<>(Set.of(employeeRole)));
        manager.setRoles(new HashSet<>(Set.of(managerRole)));
        
        task.setCompleted(false);
        task.setTitle("Do 10 pushups");
        task.setDescription("With one hand");
        
        assignment.setAssignedBy(manager);
        assignment.setAssignedTo(employee);
        assignment.setAssignedDate(Calendar.getInstance());
        assignment.setTasks(new HashSet<>(Set.of(task)));
        
        employee.setReceivedAssignments(new HashSet<>(Set.of(assignment)));
        manager.setAssignedAssignments(new HashSet<>(Set.of(assignment)));
        
        try {
        	userDAO.save(manager);
			//userDAO.save(employee);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	private static Injector initialize() {
//		// Initialize Guice injector
//        Injector injector = Guice.createInjector(new GuiceModule());
//
//        // Get jpaInitializer guice singleton instance from the injector
//        //jpaInitializer = injector.getInstance(JPAInitializer.class);
//        
//        return injector;
//	}

}
