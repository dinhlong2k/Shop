package com.example.udemywebbackend.brands;

import java.io.IOException;
import java.util.List;

import com.example.udemywebbackend.Exception.BrandNotFoundException;
import com.example.udemywebbackend.admin.Upload.AWS.AmazoneS3Util;
import com.example.udemywebbackend.categories.Category;
import com.example.udemywebbackend.categories.CategoryService;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BrandController {
    
    @Autowired
    private BrandsService brandsService;

    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/brands")
    public String brandPage(ModelMap modelMap){

        List<Brands> listBrand=brandsService.getListBrand();

        modelMap.addAttribute("listbrand", listBrand);

        return "brands/brand";
    }

    @GetMapping("/brands/page/{pageNum}")
    public String brandByPage(ModelMap modelMap,@PathVariable("pageNum") int pageNum
                                ,@Param("sortField") String sortField, @Param("sortDir") String sortDir){
                                    
    }

    @GetMapping("/brands/newBrand")
    public String createBrandPage(ModelMap modelMap){

        Brands brands=new Brands();
        List<Category> listCate=categoriesService.getListCategoryUseInForm();

        modelMap.addAttribute("brand", brands);
        modelMap.addAttribute("listCate", listCate);
        return "brands/brandForm";
    }

    @PostMapping("/brands/createBrand")
    public String createBrand(ModelMap modelMap,RedirectAttributes redirectAttributes,@ModelAttribute("brand") Brands brands,
                             @RequestParam("fileImage") MultipartFile multipartFile) throws IOException{
        
        boolean check=brandsService.checkNameBrand(brands.getName(),null);
        
        if(check){
            String ext1= FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            String fileName=AmazoneS3Util.generateFileName(ext1, "brand");
            brands.setLogo(fileName);
            Brands brandsSave=brandsService.saveBrand(brands);
            String uploadDir="brands/" + brandsSave.getId();


            AmazoneS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());

            redirectAttributes.addFlashAttribute("message", "The Brand has updated successfully");
        }

        redirectAttributes.addFlashAttribute("messsage1", "The name of Brand exits");

        return "redirect:/brands";
    }

    @GetMapping("/brands/updateBrand/{id}")
    public String getBrand(ModelMap modelMap,RedirectAttributes redirectAttributes,@PathVariable("id") int id) throws BrandNotFoundException{
        try{
            Brands getBrands=brandsService.getBrandByID(id);

            List<Category> listCate=categoriesService.getListCategoryUseInForm();

            modelMap.addAttribute("listCate", listCate);
            modelMap.addAttribute("brand", getBrands);
            return "brands/updateBrandForm";
        }catch(BrandNotFoundException ex){
            redirectAttributes.addFlashAttribute("message1", ex.getMessage());
            return "brands/brand";
        }
    
    }

    @PostMapping("/brands/updateBrand")
    public String updateBrand(@ModelAttribute("brand") Brands brands, RedirectAttributes redirectAttributes
                                ,@RequestParam("fileImage") MultipartFile multipartFile) throws IOException
    {
        Boolean check=brandsService.checkNameBrand(brands.getName(), brands.getId());

        if(check){
            if(!multipartFile.isEmpty()){
                String ext1= FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                String fileName=AmazoneS3Util.generateFileName(ext1, "brand");
                brands.setLogo(fileName);
                String uploadDir="brands/" + brands.getId();

                AmazoneS3Util.removeFolder(uploadDir);
                AmazoneS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
                
            }

            brandsService.updateBrand(brands);

            redirectAttributes.addFlashAttribute("message", "The brand have updated successfuly");
        }else{
            redirectAttributes.addFlashAttribute("message1", "Another brand used this name"); 
        }

        return "redirect:/brands";
    }

    @GetMapping("/brands/deleteBrand/{id}")
    public String deleteBrand(@PathVariable("id") int id,ModelMap modelMap,RedirectAttributes redirectAttributes) throws BrandNotFoundException{

        try {
            brandsService.deleteBrand(id);

            redirectAttributes.addFlashAttribute("message", "The brand has deleted successfully");
            
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message1",e.getMessage());
        }

        return "redirect:/brands";
    }
}
