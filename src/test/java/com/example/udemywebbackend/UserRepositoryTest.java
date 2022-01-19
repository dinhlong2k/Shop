package com.example.udemywebbackend;


import com.example.udemywebbackend.admin.Role.Role;
import com.example.udemywebbackend.admin.User.User;
import com.example.udemywebbackend.admin.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    // class duoc cung cap boi Spring Data JPA cho unit test
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser(){

        Role roleAdmin=entityManager.find(Role.class,1); //lay mot gia tri dac biet tu database
        User user =new User("dinh ","long","longboy698@gmail.com","$2a$10$yyjsnHzDp2kqpE51AfrzIegCAeZ3SPE/cSh4CbbQYumI/bOXzKcz.","0355112694");
        user.setEnabled(true);
        user.addRole(roleAdmin);

        userRepo.save(user);
    }

    @Test
    public void testGetListUser(){
        //List<User> listUser =userRepo.findAll();
//        for (User user: listUser){
//            System.out.println(user.toString());
//        }
    }

    @Test
    public void testGetUserById(){
        Optional<User> user=userRepo.findById(1);
        System.out.println(user);
    }

    @Test
    public void testUpdateUserDetails(){
        Optional<User> user=userRepo.findById(1);
        user.get().setEnabled(true);
        user.get().setPhotos("hello");
        userRepo.save(user.get());
    }

    @Test
    public void testUpdateRoleUser(){
        Optional<User> user=userRepo.findById(1);

        Role roleAdmin=new Role(1);
        Role roleUser=new Role(2);
        user.get().getRoles().remove(roleAdmin);
        user.get().addRole(roleUser);
        userRepo.save(user.get());
    }

    @Test
    public void deleteUser(){
        int id=6;

        Long idUser=userRepo.countByUserId(id);
        if(idUser !=null || idUser !=0){
            userRepo.deleteById(id);
        }

    }

    @Test
    public void disableStatusUser(){
        boolean status=false;
        userRepo.updateEnabledStatus(1,status);
    }

    @Test
    public void enabletatusUser(){
        boolean status=true;
        userRepo.updateEnabledStatus(1,status);
    }

    @Test
    public void getListUserByPage(){
        int pageNumber=1;
        int pageSize=5;
        Pageable userPage= PageRequest.of(pageNumber-1,pageSize);

        Page<User> page=userRepo.findAll(userPage);

        for( User user : page){
            System.out.println(user.toString());
        }
    }

    @Test
    public void findUserByKeyWord(){
        String keyword="long";

        int pageNumber=1;
        int pageSize=5;

        Pageable pageable=PageRequest.of(pageNumber-1,pageSize);

        Page<User> page=userRepo.findUserSearch(keyword,pageable);

        List<User> userList=page.getContent();

        for(User user : userList){
            System.out.println(user.toString());
        }
    }

}
