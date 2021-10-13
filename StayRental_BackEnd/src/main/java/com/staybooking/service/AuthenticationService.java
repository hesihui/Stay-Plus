package com.staybooking.service;

import com.staybooking.model.Authority;
import com.staybooking.model.UserRole;
import com.staybooking.model.Token;
import com.staybooking.model.User;
import com.staybooking.repository.AuthorityRepository;
import com.staybooking.util.JwtUtil;

import com.staybooking.exception.UserNotExistException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;

@Service
public class AuthenticationService {
    private AuthenticationManager authenticationManager;
    private AuthorityRepository authorityRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, AuthorityRepository authorityRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.authorityRepository = authorityRepository;
        this.jwtUtil = jwtUtil;
    }

    public Token authenticate(User user, UserRole role) throws UserNotExistException {
        // 1. check user credential
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (AuthenticationException exception) {
            throw new UserNotExistException("User Doesn't Exist");
        }
        // 2.create token if user is valid
        Authority authority = authorityRepository.findById(user.getUsername()).orElse(null);
        if (!authority.getAuthority().equals(role.name())) {
            throw new UserNotExistException("User Doesn't Exist");
        }
        // 3. return token(json file）
        return new Token(jwtUtil.generateToken(user.getUsername()));
    }

}