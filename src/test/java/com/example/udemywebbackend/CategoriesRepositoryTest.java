package com.example.udemywebbackend;

import com.example.udemywebbackend.categories.CategoriesRepository;
import com.example.udemywebbackend.categories.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoriesRepositoryTest  {

    @Autowired
    private CategoriesRepository cateRepo;

    @Test
    public void testCreateParentCategory(){
        Category category=new Category("Computers","Computers","hello.png",false);
        cateRepo.save(category);
        
    }

    @Test
    public void testCreateSubCategory(){
    //     Category parent=new Category(5);
    //     // Category desktop=new Category("Desktop","Desktop","hello.png",false,parent);
    //     // Category laptop=new Category("Laptop","Laptop","hello.png",false,parent);
    //     // Category computerComponents=new Category("Computer Components","Computer Components","hello.png",false,parent);
    //    Category camera=new Category("Camera","Camera","hello.png",false,parent);
    //    Category smartPhone=new Category("Smart Phone","Smart Phone","hello.png",false,parent);
    // //    cateRepo.saveAll(List.of(desktop,laptop,computerComponents));
    //     //  cateRepo.saveAll(List.of(camera,smartPhone));

    //     Category desktop=new Category("Memory","Memory","hello.png",false,parent);

    //     cateRepo.save(desktop);

    }

    @Test
    public void testGetCategory(){
        Category category=cateRepo.findById(1).get();

        System.out.println(category.getName());

        Set<Category> categorySet=category.getChildern();

        for (Category subCategory :categorySet){
            System.out.println(subCategory.getName());
        }
    }

    @Test
    public void testPrintHierarchicalCategories(){
        Iterable<Category> categories=cateRepo.findRootCategory(Sort.by("name").ascending());
        for (Category subCategory: categories){
            //neu mot category khong co gia tri cha
            if(subCategory.getParent() ==null){
                System.out.println(subCategory.getName());

                Set<Category> categorySet=subCategory.getChildern();

                for(Category childrenCategory: categorySet){
                    System.out.println("--" + childrenCategory.getName());
                    printChildren(childrenCategory,1);
                }

            }
        }
    }

    private void printChildren(Category parent,int sublevel){
        int newSubLevel =sublevel +1;

        Set<Category> categorySet=SortSetCategory(parent.getChildern());
        for (Category category : categorySet){
            for (int i=0; i<newSubLevel ;i++){
                System.out.print("--");
            }

            System.out.println(category.getName());

            printChildren(category,newSubLevel);
        }
    }

    //sort gia tri trong se

    @Test
    public void getListCate(){
        List<Category> categorySet= (List<Category>) cateRepo.findAll();

        for (Category category: categorySet){
            System.out.println(category.getName());
        }
    }


    //lay gia tri cac category cha va in ra
    @Test
    public void getListRootCategory(){
        List<Category> listRootCategory=cateRepo.findRootCategory(Sort.by("name").ascending());


        for(Category category: listRootCategory){
            System.out.println(category.getName());
        }
    }

    @Test
    public void testUpdateCategory(){
        Category getCategory=cateRepo.findById(1).get();
        System.out.println(getCategory.getPhotoImagePath());
    }

    @Test
    public void testCheckEmailUnique(){

        String name="Electronics";
        String alias="Computers1";
        int id=1;

        boolean checkIdNameAlias=checkIdNameAlias(name, id, alias);

        System.out.println(checkIdNameAlias);

    }

    public Boolean checkIdNameAlias(String name, Integer id, String alias) {
        
        Category category=cateRepo.findByName(name);
        Category categoryAlias=cateRepo.findByAlias(alias);
        if(id == null || id==0){
            if(category == null){
                if(categoryAlias == null) return true;
            }else return false;
        }else{
            if(category != null && category.getId() != id) return false;
            else{
                if(categoryAlias != null && categoryAlias.getId() !=id) return false;
            }
        }

        return true;
    }

    @Test
    public void print(){
        List<Category> list=getListCategoryByPage();

        for(Category category: list){
            System.out.println(category.getName());
        }
    }


    public List<Category> getListCategoryByPage() {
        String sortDir="asc";
        int pageNumber=1;
        Sort sort=Sort.by("name");
        if(sortDir.equals("asc")) sort=sort.ascending();
        else if(sortDir.equals("desc")) sort=sort.descending();

        Pageable pageable=PageRequest.of(pageNumber-1, 4, sort);

        Page<Category> pagesCategory=cateRepo.findRootCategory(pageable); //sort root category voi ten name 
        List<Category> rootCategory =pagesCategory.getContent();

        return listHierarchicalCategories(rootCategory, sortDir);
    }

    private List<Category> listHierarchicalCategories(List<Category> listRootCategory, String sortDir) {

        List<Category> listCategory=new ArrayList<>();
        for(Category category: listRootCategory){
            
            listCategory.add(Category.copyFullPropertiesCategory(category,category.getName()));

            Set<Category> childrenCategory=SortSetCategory(category.getChildern() ,sortDir);

            for(Category children: childrenCategory){
                String name="--" + children.getName();
                listCategory.add(Category.copyFullPropertiesCategory(children,name));

                listSubHierarchicalCategories(listCategory,children,1, sortDir);

            }
        }
        return listCategory;
    }
    private void listSubHierarchicalCategories(List<Category> listCategory, Category children, int sublevel, String sortDir) {
        int newSubLevel =sublevel +1;

        Set<Category> categorySet=SortSetCategory(children.getChildern(), sortDir);

        for (Category category : categorySet){
            String nameCate="";
            for (int i=0; i<newSubLevel ;i++){
                nameCate+="--";
            }
            listCategory.add(Category.copyFullPropertiesCategory(category,nameCate + category.getName()));

            listSubHierarchicalCategories(listCategory,category,newSubLevel,sortDir);
        }
    }

    private SortedSet<Category> SortSetCategory(Set<Category> set){
        return SortSetCategory(set, "asc");
    }

    private SortedSet<Category> SortSetCategory(Set<Category> set, String sortDir){
        SortedSet<Category> sortedChildren=new TreeSet<>(new Comparator<Category>() {

            @Override
            public int compare(Category cate1, Category cate2) {
                if(sortDir.equals("asc")) return cate1.getName().compareTo(cate2.getName());
                else return cate2.getName().compareTo(cate1.getName());
            } 
        });

        sortedChildren.addAll(set);
        return sortedChildren;
    }

}
