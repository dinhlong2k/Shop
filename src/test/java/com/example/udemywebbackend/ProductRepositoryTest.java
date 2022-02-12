package com.example.udemywebbackend;

import java.util.Date;
import java.util.Set;

import com.example.udemywebbackend.brands.Brands;
import com.example.udemywebbackend.categories.Category;
import com.example.udemywebbackend.product.Product;
import com.example.udemywebbackend.product.ProductImage;
import com.example.udemywebbackend.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTest {
    
    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct(){
            Brands brands=entityManager.find(Brands.class, 10);
            Category category=entityManager.find(Category.class, 15);

            Product product=new Product();
            product.setName("Samsung galaxy A31");
            product.setAlias("samsung_galaxy_a31");
            product.setShortDescription("A good smartphone from samsung");
            product.setLongDescription("THis is a very good smartphone full description");

            product.setBrands(brands);
            product.setCategory(category);

            product.setPrice(456);
            product.setCreatedTime(new Date());
            product.setUpdatedTime(new Date());

            productRepo.save(product);
    }

    @Test
    public void testCreateProduct1(){
            Brands brands=entityManager.find(Brands.class, 38);
            Category category=entityManager.find(Category.class, 6);

            Product product=new Product();
            product.setName("Dell Inspiron 3000");
            product.setAlias("Dell_Inspiron_3000");
            product.setShortDescription("A good smartphone from dell");
            product.setLongDescription("THis is a very good laptop full description");

            product.setBrands(brands);
            product.setCategory(category);

            product.setPrice(456);
            product.setCost(400);
            product.setEnabled(true);
            product.setInStock(true);
            product.setCreatedTime(new Date());
            product.setUpdatedTime(new Date());

            productRepo.save(product);
    }

    @Test
    public void testListAll(){
        Iterable<Product> listProduct=productRepo.findAll();

        for(Product product: listProduct){
            System.out.println(product.toString());
        }
    }

    @Test
    public void testGetProduct(){
        Product product=productRepo.findById(1).get();

        System.out.println(product.toString());
    }

    @Test
    public void testUpdateProduct(){
        Product product=productRepo.findById(2).get();

        product.setCost(1000);
        product.setPrice(10000);
        productRepo.save(product);
    }

    @Test
    public void testCreateProductWithImages(){
        Integer id=1;

        Product product=productRepo.findById(id).get();

        product.setMainImage("asus");
        product.addExtraImage("image 1");
        product.addExtraImage("image 2");
        product.addExtraImage("image 3");

        productRepo.save(product);
    }

    @Test
    public void testGetProductWithImage(){
        Integer id=1;

        Product product=productRepo.findById(id).get();

        System.out.println(product.getName());

        Iterable<ProductImage> productImage=product.getImages();
        for(ProductImage product2: productImage){
            System.out.println(product2.getName());
        }
    }

    @Test
    public void testGetImageProduct(){
        Product product=new Product();

        product.setMainImage("mainImage.jpg");
        product.addExtraImage("extra1.png");
        product.addExtraImage("extra2.png");
        product.addExtraImage("extra3.png");

        Set<ProductImage> productImage=product.getImages();

        for(ProductImage productImages: productImage){
            System.out.println(productImages.getName());
        }
    }

    @Test
    public void testSaveProductWithDetails(){
        Integer productId=1;

        Product product=productRepo.findById(productId).get();

        product.addDetailProduct("Device Memory","128GB");
        product.addDetailProduct("OS", "Android 10");

        productRepo.save(product);
    }
}
