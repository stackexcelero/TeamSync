package com.stackexcelero.dataAccess.utility;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.stackexcelero.dataAccess.dao.UserDAO;
import com.stackexcelero.dataAccess.dao.UserDAOImpl;

public class GuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		// Install the JpaPersistModule
        install(new JpaPersistModule("DataAccess"));

        // Bind JPAInitializer to make it eager singleton
        bind(JPAInitializer.class).asEagerSingleton();

        // Bind UserDAO to UserDAOImpl
        bind(UserDAO.class).to(UserDAOImpl.class);
    }

}
