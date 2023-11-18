package com.stackexcelero.dataAccess.service;

import java.util.List;
import java.util.Optional;

import com.stackexcelero.dataAccess.model.Task;

public interface TaskService {
	Optional<Task> find(Task taskInput);

    List<Task> findAll();

    void create(Task taskInput);

    void update(Task taskInput);

    void delete(Task taskInput);
}
