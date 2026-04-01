package com.tudo.chatify.service.impl;

import com.tudo.chatify.exception.ChatException;
import com.tudo.chatify.exception.MessageException;
import com.tudo.chatify.exception.UserException;
import com.tudo.chatify.model.Chat;
import com.tudo.chatify.model.Message;
import com.tudo.chatify.model.User;
import com.tudo.chatify.repository.ChatRepository;
import com.tudo.chatify.repository.MessageRepository;
import com.tudo.chatify.request.SendMessageRequest;
import com.tudo.chatify.service.ChatService;
import com.tudo.chatify.service.MessageService;
import com.tudo.chatify.service.UserService;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message sendMessage(SendMessageRequest request) throws UserException, ChatException {
        User user = userService.findUserById(request.getUserId());
        Chat chat = chatService.findChatById(request.getChatId());

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(request.getContent());
        message.setTimestamp(LocalDateTime.now());

        return message;
    }

    @Override
    public List<Message> getChatMessages(Integer chatId, User reqUser) throws ChatException, UserException{
        Chat chat = chatService.findChatById(chatId);
        if(!chat.getUsers().contains(reqUser)){
            throw new UserException("You are not a member of this chat.");
        }
        return messageRepository.findMessagesByChatId(chatId);
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> opt = messageRepository.findById(messageId);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new MessageException("Message not found with id " + messageId);
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws UserException, MessageException {
        Optional<Message> opt = messageRepository.findById(messageId);
        if(opt.isPresent()){
            Message message = opt.get();
            if(!message.getUser().equals(reqUser)){
                throw new UserException("You cant delete another user's message"+reqUser.getFull_name());
                }
            messageRepository.deleteById(messageId);
        }
    }
}
