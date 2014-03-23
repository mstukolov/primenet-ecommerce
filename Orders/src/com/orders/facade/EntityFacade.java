package com.orders.facade;

import org.orders.entity.UsersE;

import javax.ejb.EJB;

public class EntityFacade {

    @EJB
    private com.orders.facade.UserFacade userFacade;

    public void create(String entity){
         if(entity == "user"){
             createUser();
         }
    }
    public void save(String entity){
        if(entity == "user"){
            createUser();
        }
    }
    public void createUser(){
        UsersE userf = new UsersE();
        userf.setLogin("Create");
        userf.setPassword("password");
        userf.setRole("Admin");
        userFacade.create(userf);
    }
    public void saveUser(UsersE user){
        userFacade.edit(user);
    }
}
