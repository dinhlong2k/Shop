package com.example.udemywebbackend.admin.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/usermanager/checkEmail")
    public String checkDuplicate(@Param("email") String email){
        return userService.findEmail(email) ? "OK" : "Duplicated";
    }

    @PostMapping("/usermanager/checkEmailUpdate")
    public String checkDuplicateUpdate(@Param("id") Integer id,@Param("email") String email){
        return userService.checkIdEmail(email,id) ? "OK" : "Duplicated";
    }
}
