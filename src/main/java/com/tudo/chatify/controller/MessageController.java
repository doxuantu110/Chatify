package com.tudo.chatify.controller;

import com.tudo.chatify.exception.ChatException;
import com.tudo.chatify.exception.MessageException;
import com.tudo.chatify.exception.UserException;
import com.tudo.chatify.model.Message;
import com.tudo.chatify.model.User;
import com.tudo.chatify.request.SendMessageRequest;
import com.tudo.chatify.response.ApiResponse;
import com.tudo.chatify.service.MessageService;
import com.tudo.chatify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Message> sendMessage(@RequestBody SendMessageRequest req,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile(jwt);
        req.setUserId(user.getId());
        Message message = messageService.sendMessage(req);

        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getChatsMessages(@PathVariable("chatId") Integer chatId,
                                                          @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile(jwt);
        List<Message> messageList = messageService.getChatMessages(chatId, user);
        return new ResponseEntity<List<Message>>(messageList, HttpStatus.OK);
    }

    @DeleteMapping("/message/{id}")
    public ResponseEntity<ApiResponse> deleteMessage(@PathVariable("id") Integer messageId,
                                                 @RequestHeader("Authorization") String jwt) throws UserException, MessageException {
        User user = userService.findUserProfile(jwt);
        messageService.deleteMessage(messageId, user);
        ApiResponse res = new ApiResponse("Message is deleted sucessfully", false);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.OK);

    }
}
