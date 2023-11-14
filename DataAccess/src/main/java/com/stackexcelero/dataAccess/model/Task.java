package com.stackexcelero.dataAccess.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "task")
public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="taskId")
	private Integer taskId;
	
	@Basic
	private String title;
	
	@Basic
    private String description;
	
	@Column(name="completed", columnDefinition="BOOLEAN")
    private boolean completed;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assignment")
	private Assignment assignment;
	
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }
    
    public Task() {}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
    
}
