package smartcv.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smartcv.auth.model.*;
import smartcv.auth.security.JwtUtil;
import smartcv.auth.serviceImpl.CustomUserDetailsService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    private EmailRequest  emailRequest;

    private final AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    private JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    @ResponseBody
    @RequestMapping(value = "/C", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginReq loginReq) {


        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            String email = authentication.getName();

            User user = new User(email, "");
            String token = jwtUtil.createToken(user);
            System.out.println("token generated: " + token);
            // Fetch the user details from the database
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Assuming you have a method to get User entity by email
            User userdto = userDetailsService.findByEmail(email);

            if (userdto == null) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }

            // Generate token
            System.out.println("token generated: " + token);

            // Create LoginRes with user ID
            LoginRes loginRes = new LoginRes(email, token, userdto.getId());

            return ResponseEntity.ok(loginRes);

        } catch (BadCredentialsException e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.out.println(e);
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userDetailsService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorRes errorResponse = new ErrorRes(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while creating the user.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        User user = userDetailsService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userDetailsService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userDetailsService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the user");
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userDetailsService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the user");
        }
    }

    @PostMapping("/user/role")
    public ResponseEntity<UserRoleResponse> getUserRoleByEmail(@RequestBody EmailRequest emailRequest) {
        String email = emailRequest.getEmail();
        try {
            String role = userDetailsService.getUserRoleByEmail(email);
            if (role != null) {
                UserRoleResponse response = new UserRoleResponse(role);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
