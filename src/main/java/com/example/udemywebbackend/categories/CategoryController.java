package com.example.udemywebbackend.categories;

import com.example.udemywebbackend.admin.Exception.CategoryNotFoundException;
import com.example.udemywebbackend.admin.Upload.AWS.AmazoneS3Util;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categoriesmanager")
    public String CategoryPage(ModelMap modelMap){

        return listCateByPage(modelMap,1,"name","asc",null);
    }

    @GetMapping("/categoriesmanager/page/{pageNum}")
    public String listCateByPage(ModelMap modelMap, @PathVariable("pageNum") int pageNum,
                                 @Param("sortField") String sortField, @Param("sortDir") String sortDir
                                 @Param("keyword") String keyword){

        if(sortDir == null || sortDir.isEmpty()){
            sortDir="asc";
        }

        PageCategoryInfo pageCategoryInfo=new PageCategoryInfo();
        List<Category> pageCate=categoryService.getListCategoryByPage(pageCategoryInfo,pageNum,sortDir);

        String reverseSortDir= sortDir.equals("asc") ? "desc" : "asc";

        modelMap.addAttribute("totalPage", pageCategoryInfo.getTotalPages());
        modelMap.addAttribute("totalItems", pageCategoryInfo.getTotalElements());
        modelMap.addAttribute("currentPage", pageNum);
        modelMap.addAttribute("sortField",sortField);
        modelMap.addAttribute("sortDir",sortDir);
        modelMap.addAttribute("currentPage",pageNum);
        modelMap.addAttribute("reverseSortDir",reverseSortDir);
        modelMap.addAttribute("listCate",pageCate);

        return "categories/category";

    }

    @GetMapping(value="/categoriesmanager/newCate")
    public String newCategory(ModelMap modelMap) {

        Category category=new Category();   
        List<Category> listCategory=categoryService.getListCategoryUseInForm();
        modelMap.addAttribute("cate", category);
        modelMap.addAttribute("listCate", listCategory);

        return "categories/categoryForm";
    }


    @PostMapping(value="/categoriesmanager/saveCate")
    public String saveCategory(@ModelAttribute("cate") Category category, RedirectAttributes redirectAttributes,@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        
        String checkName=categoryService.checkIdNameAlias(category.getName(),null,category.getAlias());

        if(checkName.equals("OK")){
            String filename=null;
            if(!multipartFile.isEmpty()){
                String ext1= FilenameUtils.getExtension(multipartFile.getOriginalFilename());

                filename=AmazoneS3Util.generateFileName(ext1, "c");
            
            }
            category.setImage(filename);
            Category categorySaved=categoryService.saveCategory(category);

            String uploadDir="categories-photos/" + categorySaved.getId() ;

            AmazoneS3Util.uploadFile(uploadDir, filename, multipartFile.getInputStream());
            
            redirectAttributes.addFlashAttribute("message", "The category has been saved successfully");
        }else if(checkName.equals("DuplicateName")){
            
            redirectAttributes.addFlashAttribute("message1", "The name of category has been taken");
        }else{
            redirectAttributes.addFlashAttribute("message1", "The alias of category has been taken");
        }
        return "redirect:/categoriesmanager";
    }
    

    @GetMapping("/categoriesmanager/updateCate/{id}")
    public String getCategory(ModelMap modelMap,@PathVariable("id") int id, RedirectAttributes redirectAttributes) throws CategoryNotFoundException{
        try{
            Category getCategory=categoryService.getCategoryById(id);

            List<Category> listCategory=categoryService.getListCategoryUseInForm();
            modelMap.addAttribute("cate", getCategory);
            modelMap.addAttribute("listCate", listCategory);

            return "categories/updateCategoryForm";
        }catch(CategoryNotFoundException ex){
            redirectAttributes.addFlashAttribute("message1", ex.getMessage());
            return "redirect:/categoriesmanager";
        }
    }


    @PostMapping("/categoriesmanager/updateCate")
    public String updateCategory(@ModelAttribute("cate") Category category, RedirectAttributes redirectAttributes,
                                @RequestParam("fileImage") MultipartFile multipartFile) throws IOException, CategoryNotFoundException {
        
        String checkName=categoryService.checkIdNameAlias(category.getName(),category.getId(),category.getAlias());

        if(checkName.equals("OK")){
            String filename=null;
            if(!multipartFile.isEmpty()){
                String ext1= FilenameUtils.getExtension(multipartFile.getOriginalFilename());

                filename=AmazoneS3Util.generateFileName(ext1, "c");
                
                category.setImage(filename);
                String uploadDir="categories-photos/" + category.getId() ;
                AmazoneS3Util.removeFolder(uploadDir); 
                AmazoneS3Util.uploadFile(uploadDir, filename, multipartFile.getInputStream());
            }
            categoryService.updateCategory(category);
            
            redirectAttributes.addFlashAttribute("message", "The category has been updated successfully");
        }else if(checkName.equals("DuplicateName")){
            
            redirectAttributes.addFlashAttribute("message1", "The name of category has been taken");
        }else{
            redirectAttributes.addFlashAttribute("message1", "The alias of category has been taken");
        }
        return "redirect:/categoriesmanager";
    }
    
    @GetMapping("/categoriesmanager/enabled/{id}/{status}")
    public String updateStatusCategory(RedirectAttributes redirectAttributes,@PathVariable("id") int id, @PathVariable("status") boolean status){

        categoryService.UpdateStatusCategory(status, id);
        redirectAttributes.addFlashAttribute("message", "The category has update status successfully");
        return "redirect:/categoriesmanager";
    }

    @GetMapping("/categoriesmanager/deleteCate/{id}")
    public String deleteCategory(@PathVariable("id") int id, RedirectAttributes redirectAttributes){
        try {
            categoryService.deleteCategory(id);
            String uploadDir= "categories-photos/" + id;

            AmazoneS3Util.removeFolder(uploadDir);

            redirectAttributes.addFlashAttribute("message", "The category has delete successfully");
        } catch (CategoryNotFoundException ex) {      
            redirectAttributes.addFlashAttribute("message1", ex.getMessage());
        }

        return "redirect:/categoriesmanager";
    }
}
