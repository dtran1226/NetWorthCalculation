package com.example.networthtracking.repository;

import java.util.List;
import java.util.Optional;
import com.example.networthtracking.model.User;

public interface UserDAO {

	long addUser(User user);

	List<User> getAllUsers();

	long deleteUser(long id);

	long updateUser(long id, User user);

	Optional<User> getUser(long id);
}
