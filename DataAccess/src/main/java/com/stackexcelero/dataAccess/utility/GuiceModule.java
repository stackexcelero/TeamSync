package com.stackexcelero.dataAccess.utility;

import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.persist.jpa.JpaPersistOptions;
import com.stackexcelero.dataAccess.DataAccessApp;
import com.stackexcelero.dataAccess.dao.AssignmentDAO;
import com.stackexcelero.dataAccess.dao.AssignmentDAOImpl;
import com.stackexcelero.dataAccess.dao.RoleDAO;
import com.stackexcelero.dataAccess.dao.RoleDAOImpl;
import com.stackexcelero.dataAccess.dao.TaskDAO;
import com.stackexcelero.dataAccess.dao.TaskDAOImpl;
import com.stackexcelero.dataAccess.dao.UserDAO;
import com.stackexcelero.dataAccess.dao.UserDAOImpl;
import com.stackexcelero.dataAccess.service.AssignmentService;
import com.stackexcelero.dataAccess.service.AssignmentServiceImpl;
import com.stackexcelero.dataAccess.service.RoleService;
import com.stackexcelero.dataAccess.service.RoleServiceImpl;
import com.stackexcelero.dataAccess.service.UserService;
import com.stackexcelero.dataAccess.service.UserServiceImpl;

public class GuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		JpaPersistOptions jpaPersistOptions = JpaPersistOptions.builder().setAutoBeginWorkOnEntityManagerCreation(true).build();
		JpaPersistModule jpaModule = new JpaPersistModule("DataAccess", jpaPersistOptions);
		//jpaModule.properties(getJpaProperties()); // Make sure to include your JPA properties
		install(jpaModule);
		
		// Bind JPAInitializer to make it eager singleton
		bind(JPAInitializer.class).asEagerSingleton();
		
		bind(DataAccessApp.class);
		
		//DAO layer
		bind(UserDAO.class).to(UserDAOImpl.class);
		bind(AssignmentDAO.class).to(AssignmentDAOImpl.class);
		bind(RoleDAO.class).to(RoleDAOImpl.class);
		bind(TaskDAO.class).to(TaskDAOImpl.class);
		
		//Service Layer
		bind(UserService.class).to(UserServiceImpl.class);
		bind(RoleService.class).to(RoleServiceImpl.class);
		bind(AssignmentService.class).to(AssignmentServiceImpl.class);
		
	}
	
//	private Properties getJpaProperties() {
//        // Configure your JPA properties here
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.hbm2ddl.auto", "update");
//        properties.setProperty("hibernate.archive.autodetection", "class");
//        properties.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost/TeamSync");
//        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
//        properties.setProperty("hibernate.connection.username", "postgres");
//        properties.setProperty("hibernate.connection.password", "sviluppo");
//        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        properties.setProperty("hibernate.show_sql", "true");
//        properties.setProperty("hibernate.format_sql", "true");
//        // Add other properties as needed
//        return properties;
//    }
	
}
