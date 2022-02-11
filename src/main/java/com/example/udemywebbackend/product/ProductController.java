package com.example.udemywebbackend.product;

import java.io.IOException;
import java.util.List;
import com.example.udemywebbackend.Exception.ProductNotFoundException;
import com.example.udemywebbackend.admin.Upload.AWS.AmazoneS3Util;
import com.example.udemywebbackend.brands.Brands;
import com.example.udemywebbackend.brands.BrandsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandsRepository brandRepo;

    @GetMapping("/product")
    public String gotoProductPage(ModelMap modelMap){

        List<Product> listProduct=productService.getListProduct();

        modelMap.addAttribute("listProduct", listProduct);
        return "product/product";
    }

    @GetMapping("/product/newProduct")
    public String gotoProductForm(ModelMap modelMap){

        List<Brands> listBrand=brandRepo.findAllIdAndNameASC();
        Product product =new Product();
        product.setEnabled(true);
        product.setInStock(true);

        modelMap.addAttribute("product", product);
        modelMap.addAttribute("listBrand", listBrand);
        return "product/productForm";
    }

    @PostMapping("/product/saveProduct")
    public String saveproduct(RedirectAttributes redirectAttributes,@ModelAttribute("product") Product product,
                            @RequestParam("fileImage") MultipartFile multipartFile,
                            @RequestParam("extraImage") MultipartFile[] exMultipartFiles) throws IOException {

        boolean checkName=productService.checkNameProduct(product.getName(), null);

        if(checkName){
            product.setMainImage("hello.png");
            Product savedProduct=productService.saveProduct(product);

            saveImageProduct(exMultipartFiles, multipartFile, savedProduct);
            redirectAttributes.addFlashAttribute("message", "The product have been saved successfully");

        }else{
            redirectAttributes.addFlashAttribute("message1", "The name used in another product");
        }
        
        return "redirect:/product";
    }
    
    private void saveImageProduct(MultipartFile[] exMultipartFiles,MultipartFile multipartFile,Product product) throws IOException{

        if(!multipartFile.isEmpty()){
            String fileName=AmazoneS3Util.generateFileName(multipartFile.getOriginalFilename(), "product");

            product.setMainImage(fileName);

            String uploadDir="product-images/" +product.getId();

            AmazoneS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
        }

        if(exMultipartFiles.length >0 ){
            String uploadDir ="product-images/" +product.getId() + "/extras";
            for(MultipartFile multipartFiles: exMultipartFiles){

                if(multipartFiles.isEmpty()) continue;

                String fileName=AmazoneS3Util.generateFileName(multipartFiles.getOriginalFilename(), "product");

                product.addExtraImage(fileName);

                AmazoneS3Util.uploadFile(uploadDir, fileName, multipartFiles.getInputStream());
            }
        }

        productService.updateProductImage(product);
    }

    @GetMapping(value="/product/enabled/{id}/{status}")
    public String updateStatusProduct(@PathVariable("id") int id,@PathVariable("status") boolean status,RedirectAttributes redirectAttributes) {
        productService.updateStatus(id, status);

        redirectAttributes.addFlashAttribute("message", "The product has been enabled successfully");
        return "redirect:/product";
    }
    
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, RedirectAttributes redirectAttributes){
        try {
            String uploadDir="product-images/" + id + "/extras";
            AmazoneS3Util.removeFolder(uploadDir);

            productService.deleteProduct(id);

            redirectAttributes.addFlashAttribute("message", "Product has been deleted successfully");
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message1", e.getMessage());
        }

        return "redirect:/product";
    }

}
