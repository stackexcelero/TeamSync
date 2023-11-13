package com.stackexcelero.dataAccess.utility;

import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.persist.jpa.JpaPersistOptions;
import com.stackexcelero.dataAccess.DataAccessApp;
import com.stackexcelero.dataAccess.dao.UserDAO;
import com.stackexcelero.dataAccess.dao.UserDAOImpl;

public class GuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		JpaPersistOptions jpaPersistOptions = JpaPersistOptions.builder().setAutoBeginWorkOnEntityManagerCreation(true).build();
		JpaPersistModule jpaModule = new JpaPersistModule("DataAccess", jpaPersistOptions);
		jpaModule.properties(getJpaProperties()); // Make sure to include your JPA properties
		install(jpaModule);
		
		// Bind JPAInitializer to make it eager singleton
		bind(JPAInitializer.class).asEagerSingleton();
		
		bind(DataAccessApp.class);
		bind(UserDAO.class).to(UserDAOImpl.class);
	}
	
	private Properties getJpaProperties() {
        // Configure your JPA properties here
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.archive.autodetection", "class");
        // Add other properties as needed
        return properties;
    }
	
//	@Override
//	protected void configure() {
//		// Install the JpaPersistModule
//        install(new JpaPersistModule("DataAccess"));
//
//        // Bind JPAInitializer to make it eager singleton
//        bind(JPAInitializer.class).asEagerSingleton();
//
//        bind(DataAccessApp.class);
//        bind(UserDAO.class).to(UserDAOImpl.class);
//    }

}
