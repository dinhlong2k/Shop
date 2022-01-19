package com.example.udemywebbackend.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.example.udemywebbackend.admin.Exception.CategoryNotFoundException;

@Service
@Transactional
public class CategoryServiceImp implements CategoryService{

    @Autowired
    private CategoriesRepository cateRepo;

    @Override
    public List<Category> getListCategoryByPage(PageCategoryInfo info,int  pageNumber, String sortDir) {

        Sort sort=Sort.by("name");
        if(sortDir.equals("asc")) sort=sort.ascending();
        else if(sortDir.equals("desc")) sort=sort.descending();

        Pageable pageable=PageRequest.of(pageNumber-1, CATE_BY_PAGE, sort);

        Page<Category> pagesCategory=cateRepo.findRootCategory(pageable); //sort root category voi ten name 
        List<Category> rootCategory =pagesCategory.getContent();

        info.setTotalElements(pagesCategory.getTotalElements());
        info.setTotalPages(pagesCategory.getTotalPages());

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

    @Override
    public Category getCategoryById(int id) throws CategoryNotFoundException{
        try{
            Category category=cateRepo.findById(id).get();

            return category;
        }catch(NoSuchElementException ex){
            throw new CategoryNotFoundException("Could not find any category with ID " +id);
        }
    }

    @Override
    public List<Category> getListCategoryUseInForm() {
        List<Category> listCate=new ArrayList<>();
        Iterable<Category> categoryInb= cateRepo.findRootCategory(Sort.by("name").ascending());

        for (Category subCategory: categoryInb){
            //neu mot category khong co gia tri cha
            if(subCategory.getParent() ==null){
                
                listCate.add(Category.copyIdAndNameCate(subCategory.getId(), subCategory.getName()));

                Set<Category> setCategory=SortSetCategory(subCategory.getChildern());

                for(Category childrenCategory: setCategory){
                    String nameCate="--" + childrenCategory.getName();
                    listCate.add(Category.copyIdAndNameCate(childrenCategory.getId(), nameCate));
                    printChildren(listCate,childrenCategory,1);
                }

            }
        }

        return listCate;
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
    
    private void printChildren(List<Category> listCate,Category parent,int sublevel){
        int newSubLevel =sublevel +1;

        Set<Category> categorySet=SortSetCategory(parent.getChildern());

        for (Category category : categorySet){
            String nameCate="";
            for (int i=0; i<newSubLevel ;i++){
                nameCate+="--";
            }

            listCate.add(Category.copyIdAndNameCate(category.getId(), nameCate + category.getName()));

            printChildren(listCate,category,newSubLevel);
        }
    }

    @Override
    public Boolean checkNamelUnique(String name) {

        Category nameCate=cateRepo.findByName(name);

        if(nameCate != null){
            return false;
        }
        return true;
    }

    @Override
    public Category saveCategory(Category category) {

        return cateRepo.save(category);
    }

    @Override
    public void updateCategory(Category category) {
        Category getCategory=cateRepo.findById(category.getId()).get();

        getCategory.setName(category.getName());
        getCategory.setAlias(category.getAlias());
        
        if(category.getImage() != null){
            getCategory.setImage(category.getImage());
        }else{
            String image=getCategory.getImage();
            getCategory.setImage(image);
        }
        getCategory.setEnabled(category.isEnabled());
        getCategory.setParent(category.getParent());

        cateRepo.save(getCategory);
    }

    @Override
    public String checkIdNameAlias(String name, Integer id, String alias) {
        
        Category category=cateRepo.findByName(name);
        Category categoryAlias=cateRepo.findByAlias(alias);
        if(id == null || id==0){
            if(category == null){ // neu category tim boi name la null -> kiem tra tiep
                if(categoryAlias == null) return "OK"; // neu categoryByAlias la null va categoryByName la null tra ve hop. le.
                else return "DuplicateAlias"; //nguoc lai tra ve khong hop. le. - trung ten alias
            }else return "DuplicateName"; // neu categoryByName chua gia tri - trung ten name
        }else{
            if(category != null && category.getId() != id) return "DuplicateName"; //neu category khong la null va id khac voi id category hien tai thi tra ve trung ten
            else{
                if(categoryAlias != null && categoryAlias.getId() !=id) return "DuplicateAlias";//neu category khong la null va id khac voi id category hien tai thi tra ve trung ten
            }
        }
        return "OK";
    }

    @Override
    public void UpdateStatusCategory(boolean status, int id) {
        // TODO Auto-generated method stub
        
        cateRepo.updateStatus(id, status);;
    }

    @Override
    public void deleteCategory(int id) throws CategoryNotFoundException {
        // TODO Auto-generated method stub
        Long countById = cateRepo.countById(id);

        if (countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}
		
		cateRepo.deleteById(id);
    }
}
