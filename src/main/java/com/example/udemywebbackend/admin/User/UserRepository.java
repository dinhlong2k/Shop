package com.example.udemywebbackend.admin.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User,Integer> {

    User findByEmail(String email);

    Long countByUserId(Integer id);

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.userId= ?1")
    @Modifying
    void updateEnabledStatus(int id,boolean status);

    //Paging and Sorting interface

    @Query("SELECT u from User u WHERE CONCAT(u.userId,' ',u.email, ' ',u.firstName,' ',"+" u.lastName) LIKE %?1%" )
    public Page<User> findUserSearch(String keyword, Pageable pageable);

}
