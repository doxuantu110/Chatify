package com.tudo.chatify.service.impl;

import com.tudo.chatify.config.TokenProvider;
import com.tudo.chatify.exception.UserException;
import com.tudo.chatify.model.User;
import com.tudo.chatify.repository.UserRepository;
import com.tudo.chatify.request.UpdateUserRequest;
import com.tudo.chatify.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TokenProvider tokenProvider;

    public UserServiceImpl(UserRepository userRepository, TokenProvider tokenProvider){
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public User findUserById(Integer id) throws UserException {
        Optional<User> opt = userRepository.findById(id);

        if(opt.isPresent()){
            return opt.get();
        }
        throw new UserException("User not found with id: " + id);
    }

    @Override
    public User findUserProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);

        if(email == null) {
            throw new BadCredentialsException("recieved invalid token");
        }
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserException("user not found with email: " + email);
        }
        return user;
    }

    @Override
    public User updateUser(Integer id, UpdateUserRequest req) throws UserException {
        User user = findUserById(id);

        if(user.getFull_name() != null){
            user.setFull_name(req.getFull_name());
        }

        if(user.getProfile_picture() != null){
            user.setProfile_picture(req.getProfile_picture());
        }
        return userRepository.save(user);

    }

    @Override
    public List<User> searchUser(String query) {
        List<User> users = userRepository.searchUser(query);
        return users;
    }
}
