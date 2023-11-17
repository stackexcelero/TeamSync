package com.stackexcelero.dataAccess.service;

import java.util.List;
import java.util.Optional;

import com.stackexcelero.dataAccess.model.User;

public interface UserService{ 
	Optional<User> find(User userInput);

    List<User> findAll();

    void create(User userInput);

    void update(User userInput);

    void delete(User userInput);
}
