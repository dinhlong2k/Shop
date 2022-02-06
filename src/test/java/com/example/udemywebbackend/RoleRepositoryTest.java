package com.example.udemywebbackend;

import com.example.udemywebbackend.admin.Role.Role;
import com.example.udemywebbackend.admin.Role.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository repo;

    @Test
    public void testCreateRole(){
        Role roleAdmin=new Role("Admin","Manage Anything");
        repo.save(roleAdmin);
    }

    @Test
    public void testRoleUser(){
        Role roleSalePerson=new Role("Sale","Manage product price, customer, shipping, orders and sales report");
        Role roleEditor =new Role("Editor","Manage categories, brands, product, articles and menu");
        Role roleShipper=new Role("Shipper","View Products, Orders and update order status");
        Role roleAssistant=new Role("Assistant","Manage questions and reviews");
        repo.saveAll(List.of(roleAssistant,roleEditor,roleShipper,roleSalePerson));
    }

}
