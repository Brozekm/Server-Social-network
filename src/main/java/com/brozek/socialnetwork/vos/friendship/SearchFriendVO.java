package com.brozek.socialnetwork.vos.friendship;

import com.brozek.socialnetwork.dos.friendship.EnumFriendshipStatus;
import lombok.Getter;

@Getter
public class SearchFriendVO {

    private String email;

    private String username;

    private EnumFriendshipStatus status;

    public SearchFriendVO(String email, String username, String status) {
        this.email = email;
        this.username = username;
        if (status == null){
            this.status = null;
        }else{
            this.status = EnumFriendshipStatus.valueOf(status);
        }
    }

    public SearchFriendVO() {
    }
}
