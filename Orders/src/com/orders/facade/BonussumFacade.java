package com.orders.facade;

import org.orders.entity.Bonussum;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.logging.Logger;

@Stateless
public class BonussumFacade extends AbstractFacade<Bonussum>{
    private static Logger _log = Logger.getLogger(BonussumFacade.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public BonussumFacade() {
        super(Bonussum.class);
    }

    public Bonussum findByCustomer(String customer) {
        try{
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Bonussum.class);
            Root<Bonussum> root = cq.from(Bonussum.class);
            Predicate usr = criteriaBuilder.equal(root.get("customer"), customer);
            cq.select(root).where(usr);
            return (Bonussum)getEntityManager().createQuery(cq).getSingleResult();
        }catch(NoResultException e) {
            return null;
        }
    }
}
