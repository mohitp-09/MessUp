package com.messUp.DTO;

import lombok.Data;

@Data
public class GroupDTO {
    private Long id;
    private String name;
    private String description;
    private String createdBy;

    public GroupDTO() {
    }

    public GroupDTO(Long id, String name, String description, String createdBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
    }
}
