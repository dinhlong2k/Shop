package com.example.udemywebbackend.admin.User;

import com.example.udemywebbackend.Exception.UserNotFoundException;
import com.example.udemywebbackend.admin.Role.Role;
import com.example.udemywebbackend.admin.Upload.AWS.AmazoneS3Util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/usermanager")
    public String UserPage(ModelMap modelMap){

        return listUserByPage(modelMap,1,"firstName","asc",null);
    }

    @GetMapping("/usermanager/page/{pageNum}")
    public String listUserByPage(ModelMap modelMap, @PathVariable("pageNum") int pageNum,
                           @Param("sortField") String sortField,@Param("sortDir") String sortDir,
                            @Param("keyword") String keyword){
        Page<User> page=userService.listUserByPage(pageNum,sortField,sortDir,keyword);

        List<User> userList=page.getContent();

        long startCount= (pageNum-1) * UserService.USER_BY_PAGE +1;
        long endCount =startCount +UserService.USER_BY_PAGE -1;
        if(endCount >page.getTotalElements()){
            endCount =page.getTotalElements();
        }

        String reverseSortDir= sortDir.equals("asc") ? "desc" : "asc";

        modelMap.addAttribute("keyword",keyword);
        modelMap.addAttribute("sortField",sortField);
        modelMap.addAttribute("sortDir",sortDir);
        modelMap.addAttribute("currentPage",pageNum);
        modelMap.addAttribute("totalPage" ,page.getTotalPages());
        modelMap.addAttribute("startCount",startCount);
        modelMap.addAttribute("endCount",endCount);
        modelMap.addAttribute("reverseSortDir",reverseSortDir);
        modelMap.addAttribute("totalElement",page.getTotalElements());
        modelMap.addAttribute("listUser",userList);

        return "user/user";
    }

    @RequestMapping("/usermanager/newUser")
    public String CreateNewUser(ModelMap modelMap){
        User userRequest=new User();

        Set<Role> roleList=new HashSet<>();
        roleList=userService.getListRole();
        modelMap.addAttribute("user",userRequest);
        modelMap.addAttribute("roles",roleList);
        return "user/userForm";
    }

    @RequestMapping("/usermanager/saveUser")
    public String saveUser(@ModelAttribute("user") User userRequest, RedirectAttributes redirectAttributes,
                           @RequestParam("image")MultipartFile multipartFile) throws IOException {

        String email=userRequest.getEmail();
        boolean check=userService.findEmail(email);
        if(check){
            String fileName=null;
            if(!multipartFile.isEmpty()){
                String ext1= FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                fileName=AmazoneS3Util.generateFileName(ext1,"u");
            }
            userRequest.setPhotos(fileName);
            User savedUser=userService.saveUser(userRequest);

            String folderName="user-photos/" +savedUser.getUserId();

            AmazoneS3Util.uploadFile(folderName,fileName,multipartFile.getInputStream());

            redirectAttributes.addFlashAttribute("message","The user have been save successfully");
        }else{
            redirectAttributes.addFlashAttribute("message1","Another user is used this email");
        }
        return "redirect:/usermanager/";
    }

    @RequestMapping("/usermanager/updateUser/{id}")
    public String getUser(@PathVariable("id") Integer id,ModelMap modelMap,RedirectAttributes redirectAttributes
                            ) throws UserNotFoundException{

        try{
            User userUpdate=userService.getUserById(id);

            Set<Role> listRole=new HashSet<>();
            listRole=userService.getListRole();

            modelMap.addAttribute("roles",listRole);
            modelMap.addAttribute("user",userUpdate);

            return "user/updateUserForm";
        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message1",ex.getMessage());

            return "redirect:/usermanager/";
        }
    }

    @RequestMapping("/usermanager/updateUser")
    public String updateUserToData(@ModelAttribute("user") User userRequest,ModelMap modelMap,RedirectAttributes redirectAttributes
                                    ,@RequestParam ("image") MultipartFile multipartFile) throws UserNotFoundException,IOException{

        boolean emailCheck=userService.checkIdEmail(userRequest.getEmail(),userRequest.getUserId());

        if(emailCheck) {
            if(!multipartFile.isEmpty()){
                String ext1= FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                String fileName=AmazoneS3Util.generateFileName(ext1,"u");

                String uploadDir="user-photos/" +userRequest.getUserId();
                AmazoneS3Util.removeFolder(uploadDir);
                AmazoneS3Util.uploadFile(uploadDir,fileName,multipartFile.getInputStream());
                userRequest.setPhotos(fileName);
            }
            userService.updateUser(userRequest);

            redirectAttributes.addFlashAttribute("message","The user update has been successfully");
        }else{

            redirectAttributes.addFlashAttribute("message1","Another user is used this email");
        }
        return "redirect:/usermanager/";
    }

    @RequestMapping("/usermanager/deleteUser/{id}")
    public String DeleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, ModelMap modelMap) throws UserNotFoundException{

        try{
            String uploadDir="user-photos/" +id;
            AmazoneS3Util.removeFolder(uploadDir);
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message","Delete User succesfully");
        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message1",ex.getMessage());
        }
        return "redirect:/usermanager";
    }

    @RequestMapping("/usermanager/enabled/{id}/{status}")
    public String UpdateStatusUser(@PathVariable("status") Boolean status,@PathVariable("id") int id,RedirectAttributes redirectAttributes){

        userService.setActiveUser(id,status);

        String statusUpdate=status ?"enabled" : "disabled";
        String message ="The user have been " +statusUpdate +" successfully";

        redirectAttributes.addFlashAttribute("message",message);

        return "redirect:/usermanager";
    }


}
