package com.example.udemywebbackend.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.udemywebbackend.admin.Upload.AWS.Contants;
import com.example.udemywebbackend.brands.Brands;
import com.example.udemywebbackend.categories.Category;

import org.springframework.data.annotation.Transient;

import lombok.*;

@Entity
@Table(name="product")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,unique = true,length = 256)
    private String name;

    @Column(nullable = false,unique = true,length = 256)
    private String alias;

    @Column(nullable = false,length = 512, name="short_description")
    private String shortDescription;

    @Column(nullable = false,length = 4096, name="long_description")
    private String longDescription;

    
    @Column(name="created_time")
    private Date createdTime;
    @Column(name="updated_time")
    private Date updatedTime;

    private boolean enabled;

    @Column(name="in_stock")
    private boolean inStock;

    private float cost;
    private float price;

    @Column(name="discount_percent")
    private float discoutPercent;

    private float length;
    private float width;
    private float height;
    private float weight;

    @Column(name = "main_image",nullable = false)
    private String mainImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brands brands;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Set<ProductImage> images=new HashSet<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<ProductDetail> productDetails=new ArrayList<>();

    public void addExtraImage(String imageName){
        this.images.add(new ProductImage(imageName,this));
    }

    public void addDetailProduct(String name, String value){
        this.productDetails.add(new ProductDetail(name,value,this));
    }

    @Transient
    public String getPhotoImagePath(){
        if(mainImage==null) return "/images/thumbCate.png";

        else{
            return Contants.S3_BASE_URI + "product-images/" + this.id + "/" +this.mainImage;
        }
    }

}
