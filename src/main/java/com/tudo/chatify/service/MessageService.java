package com.tudo.chatify.service;

import com.tudo.chatify.exception.ChatException;
import com.tudo.chatify.exception.MessageException;
import com.tudo.chatify.exception.UserException;
import com.tudo.chatify.model.Message;
import com.tudo.chatify.model.User;
import com.tudo.chatify.request.SendMessageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    public Message sendMessage(SendMessageRequest request) throws UserException, ChatException;
    public List<Message> getChatMessages(Integer chatId, User reqUser) throws ChatException, UserException;
    public Message findMessageById(Integer messageId) throws MessageException;
    public void deleteMessage(Integer messageId, User reqUser) throws UserException, MessageException;

}
