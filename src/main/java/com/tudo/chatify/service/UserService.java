package com.tudo.chatify.service;

import com.tudo.chatify.exception.UserException;
import com.tudo.chatify.model.User;
import com.tudo.chatify.request.UpdateUserRequest;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public User findUserById(Integer id) throws UserException;
    public User findUserProfile(String jwt) throws UserException;
    public User updateUser(Integer id, UpdateUserRequest req) throws ExecutionControl.UserException, UserException;
    public List<User> searchUser(String query);
}
