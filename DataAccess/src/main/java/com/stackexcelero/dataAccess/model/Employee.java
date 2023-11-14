package com.stackexcelero.dataAccess.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Employee")
public class Employee extends User{
	// Additional fields and methods specific to employees
}
