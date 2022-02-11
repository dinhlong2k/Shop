package com.example.udemywebbackend.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import com.example.udemywebbackend.Exception.ProductNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImp implements ProductService{

    @Autowired
    private ProductRepository repoProduct;

    @Override
    public List<Product> getListProduct() {
        
        return (List<Product>) repoProduct.findAll();
    }

    @Override
    public Product saveProduct(Product product) {
        product.setCreatedTime(new Date());
        if(product.getAlias() == null || product.getAlias().isEmpty()){
            String productConvert =product.getName().replaceAll(" ", "_");
            product.setAlias(productConvert);
        }else{
            product.setAlias(product.getAlias().replaceAll(" ", "_"));
        }

        product.setUpdatedTime(new Date());
        return repoProduct.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        // TODO Auto-generated method stub
        Product productUpdate=repoProduct.findById(product.getId()).get();

    }

    @Override
    public Product getProductById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean checkNameProduct(String name,Integer id) {
        Product checkName=repoProduct.findByName(name);
        if(id == null) {
            if(checkName !=null) return false;
            else return true;
        }else{
            if(checkName !=null && id ==checkName.getId()) return true;
            else return false;
        }
    }

    @Override
    public void updateStatus(int id, boolean status) {
        repoProduct.updateStatus(id, status);
    }

    @Override
    public void deleteProduct(int id) throws ProductNotFoundException {
        Long count=repoProduct.countById(id);

        if(count == null || count ==0 ){
            throw new ProductNotFoundException("Couldn't find any product with id " + id);
        }

        repoProduct.deleteById(id);
        
    }

    @Override
    public void updateProductImage(Product product) {
        // TODO Auto-generated method stub
        Product productUpdate=repoProduct.findById(product.getId()).get();

        productUpdate.setMainImage(product.getMainImage());
        productUpdate.setImages(product.getImages());

        repoProduct.save(productUpdate);
    }
}
