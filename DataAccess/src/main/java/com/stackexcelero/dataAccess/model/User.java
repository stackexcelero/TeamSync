package com.stackexcelero.dataAccess.model;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "appuser", schema = "public")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="userId")
	private Integer userId;
	
	@Basic
	private String username;
	
	@Basic
	private String password;
	
	@OneToMany(mappedBy ="assignedTo", cascade = CascadeType.ALL)  
	Set<Assignment> receivedAssignments = new LinkedHashSet<Assignment>();
	
	@OneToMany(mappedBy ="assignedBy", cascade = CascadeType.ALL)  
	Set<Assignment> assignedAssignments = new LinkedHashSet<Assignment>();
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "roleId")
    )
	Set<Role> roles = new LinkedHashSet<>();

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Assignment> getReceivedAssignments() {
		return receivedAssignments;
	}

	public void setReceivedAssignments(Set<Assignment> receivedAssignments) {
		this.receivedAssignments = receivedAssignments;
	}

	public Set<Assignment> getAssignedAssignments() {
		return assignedAssignments;
	}

	public void setAssignedAssignments(Set<Assignment> assignedAssignments) {
		this.assignedAssignments = assignedAssignments;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
