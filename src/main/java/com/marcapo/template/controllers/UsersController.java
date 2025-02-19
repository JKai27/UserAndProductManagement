package com.marcapo.template.controllers;

import com.marcapo.template.documents.User;
import com.marcapo.template.dto.RegisterUserRequest;
import com.marcapo.template.dto.UserUpdateRequest;
import com.marcapo.template.exceptions.EmailORUsernameCanNotBeEmptyException;
import com.marcapo.template.exceptions.InvalidEmailFormatException;
import com.marcapo.template.repository.UserRepository;
import com.marcapo.template.exceptions.InvalidPasswordException;
import com.marcapo.template.exceptions.UserNotFoundException;
import com.marcapo.template.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UsersController {
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final UserService userService;
    UserRepository userRepository;

    @Autowired
    public UsersController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterUserRequest request, BindingResult result) throws InvalidPasswordException, EmailORUsernameCanNotBeEmptyException, UserNotFoundException {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        User savedUser = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> editUser(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateRequest request) throws UserNotFoundException, InvalidPasswordException, InvalidEmailFormatException {

        logger.info("PATCH request received for user update: ID = {}", id);
        logger.info("Received payload: {}", request);

        User updatedUser = userService.editUser(id, request);

        return ResponseEntity.ok(updatedUser);
    }

    // update it - only logged-in users should be able to delete (consider this for other methods too)
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();  // 204 No Content
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();   // 404 Not Found
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.ok("All users have been deleted");
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        try {
            User userByUsername = userService.findUserByUsername(username);
            return ResponseEntity.ok(userByUsername);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            User userByEmail = userService.findUserByEmail(email);
            return ResponseEntity.ok(userByEmail);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/all")
    public List<User> getAllUsers() {
        userRepository.findAll();
        return new ArrayList<>(userRepository.findAll());
    }

    @GetMapping("/health")
    public String health() {
        return "Up and running";
    }
}
