package com.recruiter.auth_service.service;


import com.recruiter.auth_service.model.Role;
import com.recruiter.auth_service.model.User;
import com.recruiter.auth_service.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private  final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found."));
    }

    public Role findRoleByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null ? user.getRole() : null;
    }

    public Integer findIdByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null ? user.getId() : null;
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public User getUsersById(Integer id) {
        User user = userRepository.findById(id).orElseThrow();
        return user;
    }

    public String updateUserStatus(Integer id) {
        try {
            User user = userRepository.findById(id).orElseThrow();
            user.setStatus(!user.isStatus());
            userRepository.save(user);
            return "User status updated successfully.";
        } catch (Exception e) {
            return "An error occurred while updating the user status.";
        }
    }

    public String updateUser(Integer id, User user) {
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id " + id));
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setGender(user.getGender());
            existingUser.setMobile(user.getMobile());
            // Set other fields as needed
            userRepository.save(existingUser);
            return "user edited successfully";
        } catch (Exception e) {
            // Handle specific exceptions or rethrow if necessary
            throw new RuntimeException("Failed to update user with id " + id, e);
        }
    }
}
