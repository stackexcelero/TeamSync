package com.stackexcelero.dataAccess.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Manager")
public class Manager extends User{
	// Additional fields and methods specific to manager
}
