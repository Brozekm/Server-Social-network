package com.brozek.socialnetwork.dos.posts;

import com.brozek.socialnetwork.dos.auth.AuthUserDO;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "post")
@Getter
public class PostDO {

    private static final String POST_SEQUENCE_NAME = "post_id_seq";
    private static final String OWNER_ID = "owner_id";
    private static final String TYPE = "type";
    private static final String CREATED_AT = "created_at";
    private static final String MESSAGE = "message";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =  POST_SEQUENCE_NAME)
    @SequenceGenerator(name = POST_SEQUENCE_NAME, sequenceName = POST_SEQUENCE_NAME, allocationSize = 1)
    private Integer id;

    @JoinColumn(name = OWNER_ID, nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AuthUserDO owner;

    @Enumerated(EnumType.STRING)
    @Column(
            name = TYPE,
            nullable = false
    )
    private EnumPostType type;

    @Column(
            name = CREATED_AT,
            nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = MESSAGE,
            nullable = false
    )
    private String message;

    public PostDO() {
    }

    public PostDO(AuthUserDO owner, EnumPostType type, String message) {
        this.owner = owner;
        this.type = type;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}
