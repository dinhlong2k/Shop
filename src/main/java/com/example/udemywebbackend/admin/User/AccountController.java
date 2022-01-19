package com.example.udemywebbackend.admin.User;

import com.example.udemywebbackend.admin.Exception.UserNotFoundException;
import com.example.udemywebbackend.admin.Security.UserPrincipal;
import com.example.udemywebbackend.admin.Upload.AWS.AmazoneS3Util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String accountForm(@AuthenticationPrincipal UserPrincipal userPrincipal
                            , ModelMap modelMap) throws UserNotFoundException {

        String email= userPrincipal.getUsername();
        User user=userService.getUserByEmail(email);
        modelMap.addAttribute("user",user);
        return "user/accountForm";
    }

    @PostMapping("/account/update")
    public String updateAccount(@ModelAttribute("user") User account, RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal UserPrincipal loggedUser,
                                @RequestParam("image")MultipartFile multipartFile) throws IOException {
        if(!multipartFile.isEmpty()){
            String ext1= FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            String fileName= AmazoneS3Util.generateFileName(ext1,"u");

            String uploadDir="user-photos/" + account.getUserId();

            AmazoneS3Util.removeFolder(uploadDir);
            AmazoneS3Util.uploadFile(uploadDir,fileName,multipartFile.getInputStream());
            account.setPhotos(fileName);

        }

        loggedUser.setFirstName(account.getFirstName());
        loggedUser.setLastName(account.getLastName());

        userService.updateAccount(account);
        redirectAttributes.addFlashAttribute("message","The user have been update successfully");

        return "redirect:/account";
    }

}
