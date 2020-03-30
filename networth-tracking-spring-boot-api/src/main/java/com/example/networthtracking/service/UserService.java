package com.example.networthtracking.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.example.networthtracking.model.Asset;
import com.example.networthtracking.model.Liability;
import com.example.networthtracking.model.User;
import com.example.networthtracking.repository.UserDAO;
import com.example.networthtracking.repository.UserRepository;

@Service
public class UserService {
	// Inject a singleton Repository for DB query processing in service
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/*
	 * // Singleton DAO which actually implements business logics of User private
	 * final UserDAO userDAO;
	 * 
	 * // Define specific User DAO Implement of UserDAO interface
	 * 
	 * @Autowired public UserService(@Qualifier("UserDAOImpl") UserDAO userDAO) {
	 * this.userDAO = userDAO; }
	 */

	/*
	 * public long addUser(User user) { return userDAO.addUser(user); }
	 * 
	 * public List<User> getAllUsers() { return userDAO.getAllUsers(); }
	 * 
	 * public long deleteUser(long id) { return userDAO.deleteUser(id); }
	 * 
	 * public long updateUser(long id, User user) { return userDAO.updateUser(id,
	 * user); }
	 * 
	 * public Optional<User> getUser(long id) { return userDAO.getUser(id); }
	 */

	public long addUser(User user) {
		// Set user for all assets and liabilities for bidirectional mapping
		user.getAssets().forEach(asset -> {
			asset.setUser(user);
		});
		user.getLiabilities().forEach(liability -> {
			liability.setUser(user);
		});
		userRepository.save(user);
		return user.getId();
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public long deleteUser(long id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException emptyRestulException) {
			return -1;
		}
		return id;
	}

	public long updateUser(long id, User user) {
		// Find a user to update by its Id
		Optional<User> optUser = getUser(id);
		// If that user exists
		if (optUser.isPresent()) {
			// Get that user
			User userTmp = optUser.get();
			// Get updated assets and liabilities to update
			List<Asset> assets = user.getAssets();
			List<Liability> liabilities = user.getLiabilities();
			// Clear all old Assets and Liabilities entities
			userTmp.getAssets().clear();
			userTmp.getLiabilities().clear();
			// Set updated values for the user we want to update
			userTmp.setName(user.getName());
			userTmp.getAssets().addAll(assets);
			userTmp.getLiabilities().addAll(liabilities);
			// Set user for all assets and liabilities for bidirectional mapping
			userTmp.getAssets().forEach(asset -> {
				asset.setUser(userTmp);
			});
			userTmp.getLiabilities().forEach(liability -> {
				liability.setUser(userTmp);
			});
			// Perform updating
			userRepository.save(userTmp);
			return userTmp.getId();
		}
		return -1;
	}

	public Optional<User> getUser(long id) {
		return userRepository.findById(id);
	}
}