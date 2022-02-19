package com.example.udemywebbackend.product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class ProductRestController {
    
    @Autowired
    private ProductService productService;

    @PostMapping("/product/checkUniqueNew")
    public String checkNameUnique(@Param("name") String name){

        return productService.checkNameProduct(name, null) ? "OK" : "Duplicated";
    }

    @PostMapping(value="/product/checkUniqueUpdate")
    public String postMethodName(@Param("name") String name,@Param("id") Integer id) {
        //TODO: process POST request
        
        return productService.checkNameProduct(name, id) ? "OK" : "Duplicated";
    }
    
}
