package com.messUp.DTO;

import lombok.Data;

@Data
public class FriendStatusDTO {
    private boolean canSendRequest;
    private boolean isFriend;
    private boolean isRequestPending;
}
