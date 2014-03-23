package com.orders.facade;

import org.orders.entity.Ecorescategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class EcorescategoryFacade extends AbstractFacade<Ecorescategory>{
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public EcorescategoryFacade() {
        super(Ecorescategory.class);
    }
    public List<Ecorescategory> findChildCategories(Long recid) {
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecorescategory.class);
        Root<Ecorescategory> root = cq.from(Ecorescategory.class);
        Predicate parent = criteriaBuilder.equal(root.get("parentrecid"), recid);
        cq.select(root).where(parent);
        return getEntityManager().createQuery(cq).getResultList();
    }

}
