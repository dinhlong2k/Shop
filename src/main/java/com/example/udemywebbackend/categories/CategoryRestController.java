package com.example.udemywebbackend.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {
    
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categoriesmanager/checkNew")
    public String checkDuplicateNameAndAlias(@Param("name") String name,@Param("alias") String alias){
        return categoryService.checkIdNameAlias(name, null, alias);
    }

    @PostMapping("/categoriesmanager/checkUpdate")
    public String checkDuplicateNameAndAlias(@Param("name") String name,@Param("alias") String alias,@Param("id") Integer id){

        return categoryService.checkIdNameAlias(name, id, alias);
    }

}
