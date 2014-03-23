package org.test;


import org.orders.entity.UsersE;

import javax.ejb.EJB;
import java.util.List;

public class TestCompile {
    @EJB
    private static com.orders.facade.UserFacade userFacade;
    public List<UsersE> usersList;


    public static void main(String[] args) {

        System.out.println("Hello from java!");
        for(UsersE user : userFacade.findAll()){
            System.out.print(user.getRecid());
        }
    }
}
