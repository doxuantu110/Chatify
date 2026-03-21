package com.tudo.chatify.controller;

import com.tudo.chatify.exception.UserException;
import com.tudo.chatify.model.User;
import com.tudo.chatify.request.UpdateUserRequest;
import com.tudo.chatify.response.ApiResponse;
import com.tudo.chatify.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwtToken) throws UserException {
        User user = userService.findUserProfile(jwtToken);
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/searchUser")
    public ResponseEntity<List<User>> searchUser(@RequestParam("name") String name, @RequestHeader("Authorization") String jwtToken) throws UserException{
        User user = userService.findUserProfile(jwtToken);
        List<User> userList = new ArrayList<>();
        if(user != null){
            userList = userService.searchUser(name);
        }
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest,
                                                  @RequestHeader("Authorization") String jwtToken) throws UserException, ExecutionControl.UserException {
        User user = userService.findUserProfile(jwtToken);
        userService.updateUser(user.getId(), updateUserRequest);
        ApiResponse api = new ApiResponse("User updated succesfully", true);
        return new ResponseEntity<ApiResponse>(api, HttpStatus.ACCEPTED);
    }
}
