package com.stackexcelero.dataAccess.service;

import java.util.List;
import java.util.Optional;

import com.stackexcelero.dataAccess.model.Assignment;

public interface AssignmentService {
	Optional<Assignment> find(Assignment assignmentInput);

    List<Assignment> findAll();

    void create(Assignment assignmentInput);

    void update(Assignment assignmentInput);

    void delete(Assignment assignmentInput);
}
