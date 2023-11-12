package com.stackexcelero.dataAccess;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.stackexcelero.dataAccess.utility.GuiceModule;
import com.stackexcelero.dataAccess.utility.JPAInitializer;

public class DataAccessApp {
	
	private static JPAInitializer jpaInitializer;

	public static void main(String[] args) {
		System.out.println("----INITIALIZATION----");
		
		try {
            Injector injector = initialize();
            System.out.println("hi");
            // Additional logic or application flow here...
        } catch (Exception e) {
            // Handle initialization errors
            e.printStackTrace();
        } finally {
            System.out.println("----TERMINATION----");
        }
	}

	private static Injector initialize() {
		// Initialize Guice injector
        Injector injector = Guice.createInjector(new GuiceModule());

        // Get jpaInitializer guice singleton instance from the injector
        jpaInitializer = injector.getInstance(JPAInitializer.class);
        
        return injector;
	}

}
