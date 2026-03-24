package com.tudo.chatify.controller;

import com.tudo.chatify.config.TokenProvider;
import com.tudo.chatify.exception.UserException;
import com.tudo.chatify.model.User;
import com.tudo.chatify.repository.UserRepository;
import com.tudo.chatify.request.LoginRequest;
import com.tudo.chatify.response.AuthResponse;
import com.tudo.chatify.service.CustomUserService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenProvider tokenProvider;
    private CustomUserService customUserService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider, CustomUserService customUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.customUserService = customUserService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@RequestBody @Valid User user) throws UserException {
        String email = user.getEmail();
        String password = user.getPassword();
        User isUser = userRepository.findByEmail(email);
        if(isUser != null) {
            throw new UserException("Email is already registered");
        }
        User createUser = new User();
        createUser.setEmail(email);
        createUser.setFull_name(user.getFull_name());
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createUser.setProfile_picture(user.getProfile_picture());
        userRepository.save(createUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse res = new AuthResponse(jwt, true);
        return new ResponseEntity<AuthResponse>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Authentication authentication = authenticateUser(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse res = new AuthResponse(jwt, true);
        return new ResponseEntity<AuthResponse>(res , HttpStatus.ACCEPTED);
    }

    @GetMapping("/")
    public Authentication authenticateUser(String userName, String password) {
        UserDetails userDetails = customUserService.loadUserByUsername(userName);
        if(userDetails == null){
            throw new BadCredentialsException("Invalid username.");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password.");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
