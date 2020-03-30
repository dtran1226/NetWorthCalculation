package com.example.networthtracking.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.example.networthtracking.model.User;

@Repository("UserDAOImpl")
public class UserDAOImpl implements UserDAO {
	// A list of users which works as a simulated database
	private static List<User> db = new ArrayList<User>();

	@Override
	public long addUser(User user) {
		db.add(new User(user.getId(), user.getName(), user.getAssets(), user.getLiabilities()));
		return user.getId();
	}

	@Override
	public List<User> getAllUsers() {
		return db;
	}

	@Override
	public long deleteUser(long id) {
		// Get a User by id
		Optional<User> User = getUser(id);
		// If the User exists, delete it
		if (User.isPresent()) {
			db.remove(User.get());
			return id;
		}
		return -1;
	}

	@Override
	public long updateUser(long id, User user) {
		return (long) getUser(id).map(p -> {
			// Get the index of a User to update based on its Id
			int idxOfUserToUpdate = db.indexOf(p);
			// If exist, update that User's info
			if (idxOfUserToUpdate >= 0) {
				db.set(idxOfUserToUpdate,
						new User(user.getId(), user.getName(), user.getAssets(), user.getLiabilities()));
				return id;
			}
			return -1;
		}).orElse(-1);
	}

	@Override
	public Optional<User> getUser(long id) {
		return db.stream().filter(user -> user.getId() == id).findFirst();
	}
}
