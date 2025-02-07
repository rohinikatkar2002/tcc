package com.example.serviceApp.Controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.serviceApp.entity.User;
import com.example.serviceApp.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UserController {
    @Autowired
    private UserService userService;
    
    
    @GetMapping("/alluser")
	public List<User> getalluser() {
		return userService.getallUser();
	}
    
//    @GetMapping("/dashboard")
//    public ResponseEntity<?> dashboard(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("user") == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to access this page");
//        }
//
//        User user = (User) session.getAttribute("/user");
//        return ResponseEntity.ok("Welcome to your dashboard, " + user.getUsername());
//    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
    	 try {
    	        String message = userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
    	        Map<String, String> response = new HashMap<>();
    	        response.put("message", message);
    	        return ResponseEntity.ok(response);
    	    } catch (RuntimeException e) {
    	        e.printStackTrace(); // Log for debugging
    	        Map<String, String> errorResponse = new HashMap<>();
    	        errorResponse.put("error", e.getMessage());
    	        return ResponseEntity.badRequest().body(errorResponse); // Ensure to return a bad request
    	    }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody User user) {
    	try {
            User user2 = userService.loginUser(user.getUsername(), user.getPassword());

            // Store user in session
            HttpSession session = request.getSession();
            session.setAttribute("user", user2); // Use user2 for session

            // Return a JSON response
            return ResponseEntity.ok(Collections.singletonMap("message", "Login successful"));
        } catch (RuntimeException e) {
            System.out.println("Login failed for user: " + user.getUsername());
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("Logged out successfully");
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        User user = (User) session.getAttribute("user");
        return ResponseEntity.ok(user);
    }
}
