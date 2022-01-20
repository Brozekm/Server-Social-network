package com.brozek.socialnetwork.repository;

import com.brozek.socialnetwork.dos.posts.IPostsDO;
import com.brozek.socialnetwork.dos.posts.PostDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface IPostRepository extends JpaRepository<PostDO, Integer> {

    @Query(value = "select distinct au.user_name as username, au.email as email, p.type as postType, p.created_at as createdAt, p.message as message from post p " +
            "left join friendship f on p.owner_id in (f.targetid, f.sourceid) " +
            "and (select id from auth_user where email = ?1) in (f.targetid, f.sourceid) " +
            "left join auth_user au on p.owner_id = au.id " +
            "where (f.status = 'FRIEND' or p.type = 'ANNOUNCEMENT' or au.email = ?1)" +
            "order by p.created_at desc " +
            "limit ?2 " +
            "offset ?3", nativeQuery = true)
    List<IPostsDO> getPosts(String logged, int limit, int offset);


    @Query(value = "select distinct au.user_name as username, au.email as email, p.type as postType, p.created_at as createdAt, p.message as message from post p " +
            "left join friendship f on p.owner_id in (f.targetid, f.sourceid) " +
            "and (select id from auth_user where email = ?1) in (f.targetid, f.sourceid) " +
            "left join auth_user au on p.owner_id = au.id " +
            "where (f.status = 'FRIEND' or p.type = 'ANNOUNCEMENT' or au.email = ?1)" +
            "and (p.created_at > ?2)" +
            "order by p.created_at desc ", nativeQuery = true)
    List<IPostsDO> getNewerPosts(String logged, LocalDateTime date);

}
