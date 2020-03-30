package com.example.networthtracking.api;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.networthtracking.model.User;
import com.example.networthtracking.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	// Singleton service to handle business logics of User
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public long addUser(@Valid @NotNull @RequestBody User user) {
		return userService.addUser(user);
	}

	@GetMapping
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping(path = "{id}")
	public User getUser(@PathVariable("id") long id) {
		return userService.getUser(id).orElse(null);
	}

	@DeleteMapping(path = "{id}")
	public long deleteUser(@PathVariable("id") long id) {
		return userService.deleteUser(id);
	}

	@PutMapping(path = "{id}")
	public long updateUser(@PathVariable long id, @Valid @NotNull @RequestBody User user) {
		return userService.updateUser(id, user);
	}
}