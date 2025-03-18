package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Admin;
import com.self.PureSkyn.Model.User;
import com.self.PureSkyn.repository.AdminRepo;
import com.self.PureSkyn.repository.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;
    private final AdminRepo adminRepo;

    public UserDetailsServiceImpl(UserRepo userRepo, AdminRepo adminRepo) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Admin> adminOpt = adminRepo.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(admin.getEmail())
                    .password(admin.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                    .build();
        }

        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                    .build();
        }

        throw new UsernameNotFoundException("User not found: " + email);
    }
}
