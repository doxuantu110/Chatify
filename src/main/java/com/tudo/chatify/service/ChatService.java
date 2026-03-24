package com.tudo.chatify.service;

import com.tudo.chatify.exception.ChatException;
import com.tudo.chatify.exception.UserException;
import com.tudo.chatify.model.Chat;
import com.tudo.chatify.model.User;
import com.tudo.chatify.request.GroupChatRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatService {
    public Chat createChat(User reqUser, Integer userId) throws UserException;
    public Chat findChatById(Integer chatId) throws ChatException;
    public List<Chat> findAllChatsByUserId(Integer userId) throws UserException;
    public Chat CreateGroup(GroupChatRequest req, User reqUser) throws UserException;
    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException;
    public Chat removeUserFromGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException;
    public Chat renameGroup(String grpName, Integer chatId, User reqUser) throws UserException, ChatException;
    public void deleteChat(Integer chatId, Integer userId) throws UserException, ChatException;
}
