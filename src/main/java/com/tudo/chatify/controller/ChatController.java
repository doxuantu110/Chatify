package com.tudo.chatify.controller;

import com.tudo.chatify.exception.ChatException;
import com.tudo.chatify.exception.UserException;
import com.tudo.chatify.model.Chat;
import com.tudo.chatify.model.User;
import com.tudo.chatify.request.GroupChatRequest;
import com.tudo.chatify.request.SingleChatRequest;
import com.tudo.chatify.response.ApiResponse;
import com.tudo.chatify.service.ChatService;
import com.tudo.chatify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @PostMapping("/single")
    public ResponseEntity<Chat> createChat(@RequestBody SingleChatRequest req,
                                           @RequestHeader("Authorization") String jwtToken) throws UserException{
        User reqUser = userService.findUserProfile(jwtToken);
        Chat chat = chatService.createChat(reqUser, req.getUserId());
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroup(@RequestBody GroupChatRequest req,
                                            @RequestHeader("Authorization") String jwtToken) throws UserException{
        User reUser = userService.findUserProfile(jwtToken);
        Chat chat = chatService.CreateGroup(req, reUser);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @GetMapping("/getAllChats")
    public ResponseEntity<List<Chat>> findAllChatByUserId(@RequestHeader("Authorization") String jwtToken) throws UserException {
        User reqUser = userService.findUserProfile(jwtToken);
        List<Chat> chats = chatService.findAllChatsByUserId(reqUser.getId());
        return new ResponseEntity<List<Chat>>(chats, HttpStatus.OK);
    }

    @GetMapping("/findChat/{chatId}")
    public ResponseEntity<Chat> findChatById(@PathVariable("chatId") Integer chatId) throws ChatException {
        Chat chat = chatService.findChatById(chatId);
        return new ResponseEntity<Chat>(chat , HttpStatus.OK);
    }

    @PutMapping("/addUserToGroup/{userId}/{chatId}")
    public ResponseEntity<Chat> addUserToGroup(@PathVariable("userId") Integer userId,
                                                @PathVariable("chatId") Integer chatId,
                                               @RequestHeader("Authorization") String jwtToken) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwtToken);
        Chat chat = chatService.addUserToGroup(userId, chatId, reqUser);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @PutMapping("/remove/{userId}/{chatId}")
    public ResponseEntity<Chat> removeUserFromGroup(@PathVariable("userId") Integer userId,
                                                    @PathVariable("chatId") Integer chatId,
                                                    @RequestHeader("Authorization") String jwtToken) throws UserException, ChatException{
        User reqUser = userService.findUserProfile(jwtToken);
        Chat chat = chatService.removeUserFromGroup(userId, chatId, reqUser);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @PutMapping("/renameGroup/{groupName}/{chatId}")
    public ResponseEntity<Chat> renameGroup(@PathVariable("groupName") String groupName,
                                            @PathVariable("chatId") Integer chatId,
                                            @RequestHeader("Authorization") String jwtToken) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwtToken);
        Chat chat = chatService.renameGroup(groupName, chatId, reqUser);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/deleteChat/{chatId}")
    public ResponseEntity<ApiResponse> deleteChat(@PathVariable("chatId") Integer chatId,
                                                  @RequestHeader("Authorization") String jwtToken) throws UserException, ChatException{
        User reqUser = userService.findUserProfile(jwtToken);
        chatService.deleteChat(chatId , reqUser.getId());
        ApiResponse res = new ApiResponse("Chat deleted succesfully", true);
        return new ResponseEntity<>(res , HttpStatus.OK);
    }
}
