package com.brozek.socialnetwork.repository;

import com.brozek.socialnetwork.dos.posts.IPostsDO;
import com.brozek.socialnetwork.dos.posts.PostDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPostRepository extends JpaRepository<PostDO, Integer> {

    @Query(value = "select p.message as message, p.type as type from post p " +
            "order by p.created_at " +
            "limit ?1 " +
            "offset ?2", nativeQuery = true)
    List<IPostsDO> getPosts(int limit, int offset);

}
