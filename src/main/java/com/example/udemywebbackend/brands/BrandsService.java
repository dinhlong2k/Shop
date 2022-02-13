package com.example.udemywebbackend.brands;

import java.util.List;

import com.example.udemywebbackend.Exception.BrandNotFoundException;

import org.springframework.data.domain.Page;

public interface BrandsService {
    
    public static final int USER_BY_PAGE=5;

    Brands saveBrand(Brands brands);
    List<Brands> getListBrand();
    Brands getBrandByID(int id) throws BrandNotFoundException;
    void updateBrand(Brands brands);
    boolean checkNameBrand(String name,Integer id);
    void deleteBrand(int id) throws BrandNotFoundException;
    Page<Brands> listBrandByPage(int pageNum, String sortDir, String sortField,String keyword);
}
