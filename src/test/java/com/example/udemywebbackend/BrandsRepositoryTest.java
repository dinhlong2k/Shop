package com.example.udemywebbackend;

import java.util.List;
import java.util.Set;

import com.example.udemywebbackend.brands.Brands;
import com.example.udemywebbackend.brands.BrandsRepository;
import com.example.udemywebbackend.categories.Category;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
@Transactional
public class BrandsRepositoryTest {
    
    @Autowired
    private BrandsRepository repoBrand;
    
    @Test
    public void CreateBrands(){

        Brands acer=new Brands();

        acer.setName("Acer1");
        acer.setLogo("acer.png");
        Category categories=new Category(19);
        acer.getCategories().add(categories);

        repoBrand.save(acer);
    }

    @Test
    public void CreateBrandsWithAnyCate(){

        Brands apple=new Brands();

        apple.setName("Apple1");
        apple.setLogo("apple.png");
        Category categories=new Category(11);
        Category categories1=new Category(20);
        apple.getCategories().add(categories);
        apple.getCategories().add(categories1);

        repoBrand.save(apple);
    }

    @Test
    public void getAllBrands(){
        Iterable<Brands> listBrand=repoBrand.findAll();

        for(Brands brands: listBrand){

            System.out.println(brands.getName());

            Set<Category> setCate=brands.getCategories();

            for(Category category: setCate){
                System.out.println("--" + category.getName());
            }
        }
    }

    @Test
    public void getListBrandByNameASC(){
        List<Brands> listBrand=repoBrand.findAllIdAndNameASC();

        for(Brands brands: listBrand){
            System.out.println(listBrand.toString());
        }
    }

    @Test
    public void getBrandByID(){
        int id=1;
        Brands brands=repoBrand.findById(id).get();

        System.out.println(brands.getName());

        Set<Category> setCate=brands.getCategories();

        for(Category category: setCate){
            System.out.println("--" + category.getName());
        }

        Assertions.assertThat(brands.getId()).isEqualTo(1);
    }

    @Test
    public void UpdateBrand(){
        int id=1;
        Brands brands=repoBrand.findById(id).get();

        System.out.println(brands.getName());

        Set<Category> setCate=brands.getCategories();

        for(Category category: setCate){
            System.out.println("--" + category.getName());
        }

        brands.setName("Asus");
        brands.setLogo("asus.png");
        
        Category category=new Category(21);
        brands.getCategories().add(category);

        repoBrand.save(brands);

        Assertions.assertThat(brands.getName()).isEqualTo("Asus");
    }

    @Test
    public void DeleteBrand(){
        int id=1;

        repoBrand.deleteById(id);
    }

    @Test
    public void getListBrandByPage(){
        String sortField="id";
        String sortDir="asc";
        int pageNum=1;
        Sort sort=Sort.by(sortField);

        sort=sortDir.equals("asc") ?sort.ascending()  : sort.descending();
        Pageable pageable= PageRequest.of(pageNum-1,5,sort);
        Page<Brands> pageBrands= repoBrand.findBrandSearch("can",pageable);
        
        List<Brands> listbrand=pageBrands.getContent();
        for(Brands brands: listbrand){
            System.out.println(brands.getName());
        }
    }
}
