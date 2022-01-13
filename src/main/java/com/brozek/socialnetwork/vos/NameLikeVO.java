package com.brozek.socialnetwork.vos;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
public class NameLikeVO {

    @NotNull
    @Length(min = 3, max = 20)
    private String name;

    public NameLikeVO() {
    }

    public NameLikeVO(String name) {
        this.name = name;
    }
}
