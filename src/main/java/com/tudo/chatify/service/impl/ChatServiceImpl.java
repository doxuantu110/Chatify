package com.tudo.chatify.service.impl;

import com.tudo.chatify.exception.ChatException;
import com.tudo.chatify.exception.UserException;
import com.tudo.chatify.model.Chat;
import com.tudo.chatify.model.User;
import com.tudo.chatify.repository.ChatRepository;
import com.tudo.chatify.request.GroupChatRequest;
import com.tudo.chatify.service.ChatService;
import com.tudo.chatify.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    private ChatRepository chatRepository;
    private UserService userService;

    public ChatServiceImpl(ChatRepository chatRepository, UserService userService){
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    @Override
    public Chat createChat(User reqUser, Integer userId) throws UserException {
        User userToChat = userService.findUserById(userId);
        Chat chat = chatRepository.findSingleChatByUser(userToChat, reqUser);
        if(chat != null){
            return chat;
        }
        Chat newChat = new Chat();
        newChat.setCreatedBy(reqUser);
        newChat.getUsers().add(reqUser);
        newChat.getUsers().add(userToChat);
        newChat.setIsGroup(false);
        newChat.setChatName(userToChat.getFull_name());
        newChat.setChatImage(userToChat.getProfile_picture());
        return newChat;
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if(chat.isPresent()) {
            return chat.get();
        }
        throw new ChatException("Chat not found with id "+ chatId);
    }

    @Override
    public List<Chat> findAllChatsByUserId(Integer userId) throws UserException {
        User user = userService.findUserById(userId);
        List<Chat> chatList = chatRepository.findChatsByUser(user);

        return chatList;
    }

    @Override
    public Chat CreateGroup(GroupChatRequest req, User reqUser) throws UserException {
        Chat group = new Chat();
        group.setIsGroup(true);
        group.setChatImage(req.getGroupImage());
        group.setChatName(req.getGroupName());
        group.setCreatedBy(reqUser);
        group.getAdmins().add(reqUser);
        for(Integer id : req.getUserIds()) {
            User user = userService.findUserById(id);
            group.getUsers().add(user);
        }
        group.getUsers().add(reqUser);
        chatRepository.save(group);
        return group;
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        User newUser = userService.findUserById(userId);
        if(chat.isPresent()){
            Chat grp = chat.get();
            if(grp.getAdmins().contains(reqUser)){
                grp.getUsers().add(newUser);
                return chatRepository.save(grp);
            }else{
                throw new UserException("You are not Admin!");
            }
        }
        throw new ChatException("Chat not found with id "+ chatId);
    }

    @Override
    public Chat removeUserFromGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        User userToRemove = userService.findUserById(userId);
        if(chat.isPresent()){
            Chat grp = chat.get();
            if(grp.getAdmins().contains(reqUser)){
                grp.getUsers().remove(userToRemove);
                return chatRepository.save(grp);
            }else if(grp.getUsers().contains(reqUser)){
                if(userToRemove.getId().equals(reqUser.getId())){
                    grp.getUsers().remove(userToRemove);
                    return chatRepository.save(grp);
                }
            }
            throw new UserException("You can't remove another user.");
        }
        throw new ChatException("Chat not found with id "+ chatId);

    }

    @Override
    public Chat renameGroup(String grpName, Integer chatId, User reqUser) throws UserException, ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if(chat.isPresent()){
            Chat grp = chat.get();
            if(grp.getUsers().contains(reqUser)){
                grp.setChatName(grpName);
                return chatRepository.save(grp);
            }
            throw new UserException("You are not a member of this group!");
        }
        throw new ChatException("Chat not found with id "+ chatId);
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) throws UserException, ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()) {
            Chat grp = chat.get();
            chatRepository.deleteById(chatId);
        }
    }
}
