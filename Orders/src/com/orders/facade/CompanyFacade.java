package com.orders.facade;

import org.orders.entity.Company;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CompanyFacade extends AbstractFacade<Company>{
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public CompanyFacade() {
        super(Company.class);
    }
}
