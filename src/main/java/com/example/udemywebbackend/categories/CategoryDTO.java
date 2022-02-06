package com.example.udemywebbackend.categories;

public class CategoryDTO {
    private Integer id;
    private String name;


    public CategoryDTO() {
    }


    public CategoryDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
