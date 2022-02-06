package com.example.udemywebbackend.categories;


import java.util.List;

import com.example.udemywebbackend.Exception.CategoryNotFoundException;

public interface CategoryService {

    public static final int CATE_BY_PAGE=4;

    List<Category> getListCategoryByPage(PageCategoryInfo info,int pageNumber,String sortDir,String keyword);
    Category getCategoryById(int id) throws CategoryNotFoundException;
    List<Category> getListCategoryUseInForm();
    Boolean checkNamelUnique(String name);
    Category saveCategory(Category category);
    void updateCategory(Category category);
    String checkIdNameAlias(String name,Integer id,String alias);
    void UpdateStatusCategory(boolean status, int id);
    void deleteCategory(int id) throws CategoryNotFoundException;
}
