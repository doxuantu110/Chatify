package com.tudo.chatify.repository;

import com.tudo.chatify.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m join m.chat ch WHERE ch.id = :chatId")
    public List<Message> findMessagesByChatId(Integer chatId);

    Optional<Message> findById(Integer messageId);

    void deleteById(Integer messageId);
}
