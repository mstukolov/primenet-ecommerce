package com.orders.facade;

import org.orders.entity.UsersE;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class UserFacade extends AbstractFacade<UsersE>{
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(UsersE.class);
    }
}
