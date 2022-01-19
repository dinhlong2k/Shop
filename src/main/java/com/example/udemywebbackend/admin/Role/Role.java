package com.example.udemywebbackend.admin.Role;


import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @Column(length = 40,nullable = false,unique = true)
    private String nameRole;

    @Column(length = 200,nullable = false)
    private String description;


    public Role(String nameRole, String description) {
        this.nameRole = nameRole;
        this.description = description;
    }

    public Role() {

    }

    public Role(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getNameRole() {
        return nameRole;
    }

    public void setNameRole(String nameRole) {
        this.nameRole = nameRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.nameRole;
    }
}

