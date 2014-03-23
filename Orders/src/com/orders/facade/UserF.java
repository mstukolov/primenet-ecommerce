package com.orders.facade;

import org.orders.entity.Users;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(mappedName="userF")
public class UserF extends AbstractFacade<Users>{
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
      /*  EntityManagerFactory emf = Persistence.createEntityManagerFactory("OrdersPU");
        EntityManager em = emf.createEntityManager();*/
        return em;
    }
    public UserF() {
        super(Users.class);
    }
}
