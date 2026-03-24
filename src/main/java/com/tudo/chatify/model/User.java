package com.tudo.chatify.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "full_name")
    private String full_name;

    @Column(name = "email")
    private String email;

    @Column(name = "profile_image", length = 2000)
    private String profile_picture;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @ManyToMany(mappedBy = "admins")
    private Set<Chat> groups = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private Set<Chat> chats = new HashSet<>();

    @JsonIgnore
    @OneToMany
    private List<Message> messages = new ArrayList<>();

    
}
