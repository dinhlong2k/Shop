package com.example.udemywebbackend.admin.User;

import com.example.udemywebbackend.Exception.UserNotFoundException;
import com.example.udemywebbackend.admin.Role.Role;
import com.example.udemywebbackend.admin.Role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public Page<User> listUserByPage(int pageNumber, String sortField, String sortDir, String keyword) {

        Sort sort=Sort.by(sortField);

        sort=sortDir.equals("asc") ?sort.ascending()  : sort.descending();

        Pageable pageable= PageRequest.of(pageNumber-1,USER_BY_PAGE,sort);


        if(keyword == null){
            return userRepo.findAll(pageable);
        }else{
            return userRepo.findUserSearch(keyword,pageable);
        }
    }

    @Override
    public User saveUser(User userRequest) {
        User userSave=new User(userRequest.getFirstName(),userRequest.getLastName(),userRequest.getEmail(),
                encoder().encode(userRequest.getPassword()),userRequest.getPhoneNumber());
        userSave.setRoles(userRequest.getRoles());
        userSave.setEnabled(userRequest.isEnabled());
        userSave.setPhotos(userRequest.getPhotos());
        return userRepo.save(userSave);
    }

    @Override
    public void updateUser(User user) {
        User userUpdate=userRepo.findById(user.getUserId()).get();
        String password=user.getPassword();
        userUpdate.setFirstName(user.getFirstName());
        userUpdate.setLastName(user.getLastName());
        userUpdate.setEmail(user.getEmail());
        userUpdate.setPhoneNumber(user.getPhoneNumber());

        if(user.getPhotos() != null){
            userUpdate.setPhotos(user.getPhotos());
        }else{
            String photo=userUpdate.getPhotos();
            userUpdate.setPhotos(photo);
        }

        userUpdate.setRoles(user.getRoles());

        if(!password.isEmpty()){
            userUpdate.setPassword(encoder().encode(user.getPassword()));
        }

        userRepo.save(userUpdate);
    }

    @Override
    public void deleteUser(int id) throws UserNotFoundException{
        Long idUser=userRepo.countByUserId(id);

        if(idUser == null || idUser ==0){
            throw new UserNotFoundException("Couldn't find any user with ID " +id);
        }

        userRepo.deleteById(id);
    }

    @Override
    public void setActiveUser(int id, boolean status) {

        userRepo.updateEnabledStatus(id,status);
    }

    @Override
    public boolean findEmail(String email) {
        User checkEmail=userRepo.findByEmail(email);

        if(checkEmail !=null){
            return false;
        }else return true;
    }

    @Override
    public User getUserById(int id) throws UserNotFoundException {
        try{
            User user=userRepo.findById(id).get();
            return user;
        }catch (NoSuchElementException ex){
            throw new UserNotFoundException("Could not find any user with ID " +id);
        }
    }

    @Override
    public Set<Role> getListRole() {
        List<Role> listRoles=roleRepo.findAll();

        Set<Role> setRoles=new HashSet<>(listRoles);
        return  setRoles;
    }

    @Override
    public boolean checkIdEmail(String email, Integer id) {
        User findUser=userRepo.findByEmail(email);

        if(findUser == null){
            return true;
        }else{
            if(findUser.getUserId() ==id) return true;
            else return false;
        }
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        try{
            User user=userRepo.findByEmail(email);
            return user;
        }catch (NoSuchElementException ex){
            throw new UserNotFoundException("Could not find any user with email "+ email);
        }
    }

    @Override
    public void updateAccount(User account) {
        User user=userRepo.findById(account.getUserId()).get();

        if(!account.getPassword().isEmpty()){
            user.setPassword(encoder().encode(account.getPassword()));
        }

        if(account.getPhotos() != null){
            user.setPhotos(account.getPhotos());
        }

        user.setFirstName(account.getFirstName());
        user.setLastName(account.getLastName());
        user.setPhoneNumber(account.getPhoneNumber());

        userRepo.save(user);
    }
}
