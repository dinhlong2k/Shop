package com.example.udemywebbackend.product;

import java.util.List;

import com.example.udemywebbackend.Exception.ProductNotFoundException;

public interface ProductService {
    List<Product> getListProduct();
    Product saveProduct(Product product);
    void updateProduct(Product product);
    Product getProductById(int id);
    boolean checkNameProduct(String name,Integer id);
    void updateStatus(int id,boolean status);
    void deleteProduct(int id) throws ProductNotFoundException;
}
