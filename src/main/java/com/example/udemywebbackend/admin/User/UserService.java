package com.example.udemywebbackend.admin.User;

import com.example.udemywebbackend.Exception.UserNotFoundException;
import com.example.udemywebbackend.admin.Role.Role;
import org.springframework.data.domain.Page;
import java.util.Set;

public interface UserService {

    public static final int USER_BY_PAGE=5;

    Page<User> listUserByPage(int pageNumber, String sortField, String sortDir, String keyword);
    User saveUser(User user);
    void updateUser(User user);
    void deleteUser(int id) throws UserNotFoundException;
    void setActiveUser(int id, boolean status);
    boolean findEmail(String email);
    User getUserById(int id) throws UserNotFoundException;
    Set<Role> getListRole();
    boolean checkIdEmail(String email,Integer id);
    User getUserByEmail(String email) throws UserNotFoundException;
    void updateAccount(User account);
}
