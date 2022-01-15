package com.brozek.socialnetwork.dos;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "friendship")
@Getter
public class FriendshipDO {

    private static final String FSHIP_SEQUENCE_NAME = "friendship_id_seq";
    private static final String STATUS = "status";
    private static final String SOURCE_ID = "sourceid";
    private static final String TARGET_ID = "targetid";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = FSHIP_SEQUENCE_NAME)
    @SequenceGenerator(name = FSHIP_SEQUENCE_NAME, sequenceName = FSHIP_SEQUENCE_NAME, allocationSize = 1)
    private Integer id;


    @JoinColumn(name = SOURCE_ID, nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AuthUserDO source;

    @JoinColumn(name = TARGET_ID, nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AuthUserDO target;

    @Enumerated(EnumType.STRING)
    @Column(
            name = STATUS,
            nullable = false
    )
    private EnumFriendshipStatus status;

    @Column(
            name = CREATED_AT,
            nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = UPDATED_AT,
            nullable = false
    )
    private LocalDateTime updatedAt;

    public FriendshipDO() {
    }

    public FriendshipDO(AuthUserDO source, AuthUserDO target) {
        this.source = source;
        this.target = target;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = EnumFriendshipStatus.NEW;
    }

    public void setStatus(EnumFriendshipStatus status) {
        this.status = status;
    }
}
