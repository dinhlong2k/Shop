package com.example.udemywebbackend.product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
    
    @Autowired
    private ProductService productService;

    @PostMapping("/product/checkUniqueNew")
    public String checkNameUnique(@Param("name") String name){

        return productService.checkNameProduct(name, null) ? "OK" : "Duplicated";
    }
}
