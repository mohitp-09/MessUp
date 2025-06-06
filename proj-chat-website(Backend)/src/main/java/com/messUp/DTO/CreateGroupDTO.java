package com.messUp.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CreateGroupDTO {
    private String groupName;
    private String createdBy;
    private List<String> memberUsernames;
}
