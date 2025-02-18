package com.marcapo.template.service;

import com.marcapo.template.documents.User;
import com.marcapo.template.dto.RegisterUserRequest;
import com.marcapo.template.dto.UserUpdateRequest;
import com.marcapo.template.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String PASSWORD_PATTERN =
            "^(?!.*[\\s%$§°^;`\"#€~<>|])(?!.*Bendoe)(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@!%*?&])[A-Za-z\\d@!%*?&]{6,}$";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(RegisterUserRequest request) throws InvalidPasswordException {
        validateUserDoesNotExist(request.getUsername(), request.getEmail());
        validatePassword(request.getPassword(), request.getFirstName());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBirthDate(request.getBirthDate());

        return userRepository.save(user);
    }

    public User editUser(String id, UserUpdateRequest request) throws UserNotFoundException, InvalidPasswordException {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {

            // ✅ Corrected the password validation condition
            if (!request.getNewPassword().trim().matches(PASSWORD_PATTERN)) {
                throw new InvalidPasswordException("Password does not meet security requirements.");
            }

            existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        if (request.getUsername() != null) existingUser.setUsername(request.getUsername());
        if (request.getEmail() != null) existingUser.setEmail(request.getEmail());
        if (request.getFirstName() != null) existingUser.setFirstName(request.getFirstName());
        if (request.getLastName() != null) existingUser.setLastName(request.getLastName());
        if (request.getBirthDate() != null) existingUser.setBirthDate(request.getBirthDate());

        return userRepository.save(existingUser);
    }

    private void validateUserDoesNotExist(String username, String email) {
        userRepository.findByUsername(username)
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException("Username already exists, hence not available");
                });

        userRepository.findByEmail(email)
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException("Email already exists, hence not available");
                });
    }

    private void validatePassword(String password, String firstName) throws InvalidPasswordException {

        logger.info("Validating password manually: {}", password);

        if (password == null || password.trim().isEmpty()) {
            throw new InvalidPasswordException("Password cannot be empty.");
        }

        if (!password.matches(PASSWORD_PATTERN)) {
            logger.error("Password does not meet security requirements: {}", password);
            throw new InvalidPasswordException("Password does not meet security requirements.");
        }

        if (firstName != null && password.toLowerCase().contains(firstName.toLowerCase())) {
            logger.error("Password contains first name: {}", password);
            throw new InvalidPasswordException("Password cannot contain your first name.");
        }

        logger.info("Password is valid.");
    }

    public User findUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }

    public User findUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    public void deleteUser(String id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }


}
