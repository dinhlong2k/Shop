package com.example.udemywebbackend.categories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends PagingAndSortingRepository<Category,Integer> {

    @Query("SELECT c from Category c WHERE c.parent.id is NULL")
    public Page<Category> findRootCategory(Pageable pageable);

    Category findByName(String name);

    //list ra cac category cha
    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    public List<Category> findRootCategory(Sort sort);

    @Query("UPDATE Category c SET c.enabled= ?2 WHERE c.id= ?1")
    @Modifying
    void updateStatus(int id,boolean status);

    Category findByAlias(String alias);

    Long countById(Integer id);

    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
    public Page<Category> searchCategory(String keyword,Pageable pageable);
}
