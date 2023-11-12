module dataAccess {
	
	exports com.stackexcelero.dataAccess.service;
	exports com.stackexcelero.dataAccess.utility;

	requires java.se;
	requires org.postgresql.jdbc;
	
	requires org.hibernate.orm.core;
	requires jakarta.persistence;
	requires jakarta.inject;

	requires com.google.guice;
	requires com.google.guice.extensions.persist;
	
	opens com.stackexcelero.dataAccess.model to org.hibernate.orm.core;
	opens com.stackexcelero.dataAccess.dao to org.hibernate.orm.core, com.google.guice, jakarta.persistence, org.postgresql.jdbc, com.google.guice.extensions.persist, jakarta.inject;
	opens com.stackexcelero.dataAccess.utility to com.google.guice, com.google.guice.extensions.persist, jakarta.inject, org.postgresql.jdbc;
}