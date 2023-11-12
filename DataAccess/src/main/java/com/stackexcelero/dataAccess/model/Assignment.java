package com.stackexcelero.dataAccess.model;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * 
 */
@Entity
@Table(name = "assignment")
public class Assignment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="assignmentId")
	private Integer assignmentId;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar assignedDate;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar completedDate;
	
	@Column(name = "initialEstimation")
	@JdbcTypeCode(SqlTypes.LONGVARCHAR)
	private Long initialEstimation;
	
	@Column(name = "updatedEstimation")
	@JdbcTypeCode(SqlTypes.LONGVARCHAR)
	private Long updatedEstimation ;
	
	@ManyToOne
	@JoinColumn(name = "assignedTo")
	private User assignedTo;
	
	@ManyToOne
	@JoinColumn(name = "assignedBy")
	private User assignedBy;
	
	@OneToMany(mappedBy ="assignment", cascade = CascadeType.ALL)  
	Set<Task> tasks = new LinkedHashSet<Task>();

	public Integer getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Integer assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Calendar getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Calendar assignedDate) {
		this.assignedDate = assignedDate;
	}

	public Calendar getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Calendar completedDate) {
		this.completedDate = completedDate;
	}

	public Long getInitialEstimation() {
		return initialEstimation;
	}

	public void setInitialEstimation(Long initialEstimation) {
		this.initialEstimation = initialEstimation;
	}

	public Long getUpdatedEstimation() {
		return updatedEstimation;
	}

	public void setUpdatedEstimation(Long updatedEstimation) {
		this.updatedEstimation = updatedEstimation;
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public User getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(User assignedBy) {
		this.assignedBy = assignedBy;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}
}
