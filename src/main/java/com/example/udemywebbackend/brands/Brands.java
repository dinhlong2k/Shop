package com.example.udemywebbackend.brands;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.example.udemywebbackend.admin.Upload.AWS.Contants;
import com.example.udemywebbackend.categories.Category;

@Entity(name="brands")
public class Brands {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 128)
    private String logo;

    @Column(nullable = false, length = 45, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(name = "brands_category",
                joinColumns = @JoinColumn(name = "brands_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories=new HashSet<>();


    public Brands() {
    }


    public Brands(Integer id,String name) {
        this.id=id;
        this.name=name;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", logo='" + getLogo() + "'" +
            ", name='" + getName() + "'" +
            ", categories='" + getCategories() + "'" +
            "}";
    }


    @Transient
    public String getPhotoImagePath(){
        if(id==null || logo==null) return "/images/thumbnail.png";

        else{
            return Contants.S3_BASE_URI + "brands/" + this.id + "/" +this.logo;
        }
    }
}
