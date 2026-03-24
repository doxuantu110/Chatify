package com.tudo.chatify.repository;

import com.tudo.chatify.model.Chat;
import com.tudo.chatify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    @Query("select ch from Chat ch where :user MEMBER OF ch.users")
    public List<Chat> findChatsByUser(@Param("user") User user);

    @Query("select ch from Chat ch where ch.isGroup = false and :user MEMBER OF ch.users and :reqUser MEMBER OF ch.users")
    public Chat findSingleChatByUser(@Param("user") User user, @Param("reqUser") User reqUser);

}
