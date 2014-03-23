package com.orders.facade;

import org.orders.entity.Customer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
public class CustomerFacade extends AbstractFacade<Customer>{
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public CustomerFacade() {
        super(Customer.class);
    }

    public Customer findCustomer(String user) {
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> root = cq.from(Customer.class);
        Predicate usr = criteriaBuilder.equal(root.get("user"), user);
        cq.select(root).where(usr);
        return (Customer)getEntityManager().createQuery(cq).getSingleResult();
    }
}
