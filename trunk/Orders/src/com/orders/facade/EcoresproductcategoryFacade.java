package com.orders.facade;

import org.orders.entity.Ecorescategory;
import org.orders.entity.Ecoresproductcategory;
import org.orders.entity.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class EcoresproductcategoryFacade extends AbstractFacade<Ecoresproductcategory>{
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    private static Logger log = Logger.getLogger(EcoresproductcategoryFacade.class.getName());
    public EntityManager getEntityManager() {
        return em;
    }

    public EcoresproductcategoryFacade() {
        super(Ecoresproductcategory.class);
    }

    public void create(Ecorescategory ecorescategory, Product product){
        Ecoresproductcategory ecoresproductcategory = new Ecoresproductcategory();
        ecoresproductcategory.setProduct(product.getRecid());
        ecoresproductcategory.setCategory(ecorescategory.getRecid());
        ecoresproductcategory.setCreatedBy("System");
        ecoresproductcategory.setUpdatedBy("System");
        ecoresproductcategory.setCreatedAt(new Date());
        ecoresproductcategory.setUpdatedAt(new Date());
        super.create(ecoresproductcategory);
    }

    public List<Ecoresproductcategory> findByCategory(Long recid) {
        log.info("Поиск продуктов по категории:" + recid);
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecoresproductcategory.class);
        Root<Ecoresproductcategory> root = cq.from(Ecoresproductcategory.class);
        Predicate category = criteriaBuilder.equal(root.get("category"), recid);
        cq.select(root).where(category);
        return getEntityManager().createQuery(cq).getResultList();
    }
}
