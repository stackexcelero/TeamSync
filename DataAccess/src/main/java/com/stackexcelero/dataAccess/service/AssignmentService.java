package com.stackexcelero.dataAccess.service;

import java.util.List;
import java.util.Optional;

import com.stackexcelero.dataAccess.model.Assignment;
import com.stackexcelero.dataAccess.model.User;

public interface AssignmentService {
	Optional<Assignment> find(Assignment assignmentInput);

    List<Assignment> findAll();

    void create(Assignment assignmentInput);

    void update(Assignment assignmentInput);

    void delete(Assignment assignmentInput);
    
    void updateExecutor(Assignment assignment, User executor);
    void updateInitiator(Assignment assignment, User initiator);
}
