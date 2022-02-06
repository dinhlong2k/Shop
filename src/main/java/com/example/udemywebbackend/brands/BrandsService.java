package com.example.udemywebbackend.brands;

import java.util.List;

import com.example.udemywebbackend.Exception.BrandNotFoundException;

public interface BrandsService {
    
    Brands saveBrand(Brands brands);
    List<Brands> getListBrand();
    Brands getBrandByID(int id) throws BrandNotFoundException;
    void updateBrand(Brands brands);
    boolean checkNameBrand(String name,Integer id);
    void deleteBrand(int id) throws BrandNotFoundException;
}
