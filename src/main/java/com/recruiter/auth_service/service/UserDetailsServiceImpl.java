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
}
