package com.tudo.chatify.repository;

import com.tudo.chatify.exception.UserException;
import com.tudo.chatify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
        User findByUsername(String username);
        User findByEmail(String email) throws UserException;
        @Query("SELECT u FROM User u WHERE u.full_name Like %:query% OR u.email Like %:query%")
        public List<User> searchUser(@Param("Query") String query);

}
