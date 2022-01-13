package com.brozek.socialnetwork.repository;

import com.brozek.socialnetwork.dos.FriendshipDO;
import com.brozek.socialnetwork.dos.ISearchResultDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IFriendshipRepository extends JpaRepository<FriendshipDO, Integer> {

    @Query(value = "select au.email as email, au.user_name as userName, f.status as friendStatus from auth_user au " +
            "left join friendship f on au.id in (f.sourceid, f.targetid) " +
            "and (select id from auth_user where au.email = ?2) in (f.targetid, f.sourceid) " +
            "where lower(au.user_name) like ?1 and (f.status is null or f.status = 'NEW') and (au.email != ?2)", nativeQuery = true)
    List<ISearchResultDO> searchForUsernameLike(String partName, String loggedUserEmail);

    //TODO ADD STATUS
    @Query(value = "select au.email as email, au.user_name as userName from auth_user au " +
            "inner join friendship f on au.id = f.sourceid " +
            "and (select id from auth_user where email = ?1) = f.targetid " +
            "where f.status = 'NEW'", nativeQuery = true)
    List<ISearchResultDO> getUsersFriendshipRequest(String email);


    @Query(value = "select case when count(f) > 0 then true else false end from friendship f " +
            "where (f.source.email = ?1 or f.target.email = ?1)" +
            "and (f.source.email = ?2 or f.target.email = ?2)")
    boolean doUsersHaveRelationship(String firstUser, String secUser);

}
