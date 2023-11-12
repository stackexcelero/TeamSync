package com.stackexcelero.dataAccess.utility;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;

@Singleton
public class JPAInitializer {
	private final PersistService persistService;
	
	@Inject
	public JPAInitializer(PersistService persistService) {
		this.persistService = persistService;
        initializePersistence();
        // Add any additional initialization logic here...
        
    }

    private void initializePersistence() {
    	try {
            this.persistService.start();
            // You can perform additional initialization related to persistence if needed
        } catch (Exception e) {
            // Handle exceptions during persistence initialization (log, throw, etc.)
            e.printStackTrace();
            // Optionally, rethrow the exception or take appropriate action
        }
    }
	
}
