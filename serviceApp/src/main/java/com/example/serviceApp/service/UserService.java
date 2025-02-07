package com.example.serviceApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.serviceApp.entity.User;
import com.example.serviceApp.repo.UserRepository;

@Service
public class UserService {
	@Autowired
	private  UserRepository userRepository;
	public String registerUser(String username, String email, String password) {
		if (userRepository.existsByUsername(username)) {
	        throw new RuntimeException("Username is already taken");
	    }

	    if (userRepository.existsByEmail(email)) {
	        throw new RuntimeException("Email is already taken");
	    }

	    User user = new User();
	    user.setUsername(username);
	    user.setEmail(email);
	    user.setPassword(password); // Hash the password before saving

	    userRepository.save(user);
	    return "User registered successfully";
	}
	
	 public User loginUser(String username, String password) {
	        User user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

	        if (password.equals(user.getPassword())) {
	            return user;
	        } else {
	            throw new RuntimeException("Invalid username or password");
	        }
	        }

	public List<User> getallUser() {
		return userRepository.findAll();
	}
	}


