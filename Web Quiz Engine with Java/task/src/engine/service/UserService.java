package engine.service;

import engine.model.User;
import engine.repository.UserRepository;
import engine.exception.UsernameBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing user data. This includes loading user details,
 * registering new users, and checking for the existence of users by email.
 * Implements Spring Security's {@link UserDetailsService} for integration with Spring Security.
 */
@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs a UserService with necessary dependencies.
     *
     * @param repository The user repository for database access.
     * @param passwordEncoder The encoder for hashing user passwords.
     */
    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Loads the user details based on the email address. This method is used by Spring Security
     * during the authentication process.
     *
     * @param email The email of the user to load.
     * @return UserDetails The user details for the given email.
     * @throws UsernameNotFoundException If no user is found with the given email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /**
     * Registers a new user in the system. This method also handles password encryption.
     *
     * @param user The user object containing the details to register.
     * @return User The registered user with encrypted password.
     * @throws UsernameBadRequestException If the email is already taken.
     */
    public User registerUser(User user) {
        if (existsByEmail(user.getEmail())) {
            throw new UsernameBadRequestException("Email address " + user.getEmail() + " is already taken.");
        }
        user.setPassword(encodePassword(user.getPassword()));
        return repository.save(user);
    }

    /**
     * Encodes the password using the configured password encoder.
     *
     * @param password The password to encode.
     * @return String The encoded password.
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Checks if an email exists in the user repository.
     *
     * @param email The email to check.
     * @return boolean True if the email exists, false otherwise.
     */
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user to find.
     * @return Optional<User> An Optional containing the found user, if any.
     */
    public Optional<User> findByEmail(String email) { return repository.findByEmail(email);}


}
