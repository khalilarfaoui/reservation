package smartcv.auth.serviceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import smartcv.auth.model.User;
import smartcv.auth.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public void createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public String getUserRoleByEmail(String email) {
        User user = userRepository.findByEmail(email); // Assuming userRepository is available
        if (user != null) {
            return user.getRole(); // Assuming the User model has a method to get the role
        } else {
            return null;
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFirstName(userDetails.getFirstName());
            existingUser.setLastName(userDetails.getLastName());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPassword(userDetails.getPassword()); // Ensure password is hashed
            existingUser.setRole(userDetails.getRole());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}
