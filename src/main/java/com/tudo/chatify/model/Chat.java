package com.tudo.chatify.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "chat")

public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "chat_name")
    private String chatName;

    @Column(name = "chat_image")
    private String chatImage;

    @Column(name = "is_group")
    private Boolean isGroup;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToMany
    @JoinTable(name = "chat_admin",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id"))
    private Set<User> admins = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "chat_user",
                joinColumns = @JoinColumn(name = "chat_id"),
                inverseJoinColumns = @JoinColumn(name="user_id"))
    private Set<User> users = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "chat")
    private List<Message> messages = new ArrayList<>();
}
