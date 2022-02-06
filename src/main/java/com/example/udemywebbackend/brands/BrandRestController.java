package com.example.udemywebbackend.brands;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.example.udemywebbackend.Exception.BrandNotFoundException;
import com.example.udemywebbackend.categories.Category;
import com.example.udemywebbackend.categories.CategoryDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class BrandRestController {
    
    @Autowired
    private BrandsService brandsService;

    @PostMapping("/brands/checkName")
    public String CheckName(@Param("name") String name) {
        
        return brandsService.checkNameBrand(name,null) ? "OK" : "Duplicated";
    }

    @PostMapping("/brands/checkUpdate")
    public String CheckNameUpdate(@Param("name") String name,@Param("id") Integer id) {
        
        return brandsService.checkNameBrand(name,id) ? "OK" : "Duplicated";
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable("id") Integer idBrand) throws BrandNotFoundException{
        try {
            List<CategoryDTO> listCategories=new ArrayList<>();
            Brands brands=brandsService.getBrandByID(idBrand);

            Set<Category> setCate=brands.getCategories();
            for(Category category: setCate){
                CategoryDTO categoryDTO=new CategoryDTO(category.getId(),category.getName());
                listCategories.add(categoryDTO);
            }

            return listCategories;

        } catch (BrandNotFoundException e) {
            throw new BrandNotFoundException();
        }
    }
    
}
