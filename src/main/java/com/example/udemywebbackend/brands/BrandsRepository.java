package com.example.udemywebbackend.brands;

import java.util.List;

import org.springframework.data.jpa.repository.Query; 
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandsRepository extends PagingAndSortingRepository<Brands,Integer>{

    Brands findByName(String name);
    Long countById(Integer id);

    @Query("SELECT NEW brands(b.id,b.name) FROM brands b ORDER BY b.name ASC")
    public List<Brands> findAllIdAndNameASC();
}
