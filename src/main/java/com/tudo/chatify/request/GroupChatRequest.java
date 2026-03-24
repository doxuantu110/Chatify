package com.tudo.chatify.request;

import lombok.Data;

import java.util.List;
@Data
public class GroupChatRequest {
    private List<Integer> userIds;
    private String groupName;
    private String groupImage;
}
