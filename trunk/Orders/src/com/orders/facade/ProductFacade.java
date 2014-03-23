package com.orders.facade;

import attributes.core.EcoresattributeFacade;
import attributes.core.EcorescategoryattributegroupFacade;
import attributes.core.EcoresgroupbyattributeFacade;
import attributes.model.Ecoresattribute;
import attributes.model.Ecorescategoryattributegroup;
import attributes.model.Ecoresgroupbyattribute;
import org.orders.entity.Ecoresproductcategory;
import org.orders.entity.Product;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class ProductFacade extends AbstractFacade<Product>{
    private static Logger _log = Logger.getLogger(ProductFacade.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    @EJB
    private EcoresproductcategoryFacade ecoresproductcategoryFacade;
    @EJB
    private EcorescategoryattributegroupFacade ecorescategoryattributegroupFacade;
    @EJB
    private EcoresgroupbyattributeFacade ecoresgroupbyattributeFacade;
    @EJB
    private EcoresattributeFacade ecoresattributeFacade;

    public EntityManager getEntityManager() {
        return em;
    }

    public ProductFacade() {
        super(Product.class);
    }

    public List<Ecoresproductcategory> findProductCategories(Product product){
        _log.info("Поиск групп категрии в которые входит продукт: " + product.getName());
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecoresproductcategory.class);
        Root<Ecoresproductcategory> root = cq.from(Ecoresproductcategory.class);
        Predicate category = criteriaBuilder.equal(root.get("product"), product.getRecid());
        cq.select(root).where(category);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<Ecorescategoryattributegroup> findAttributeGroupsByCategory(Ecoresproductcategory ecoresproductcategory){
        try{
            _log.info("Поиск групп атрибутов по категории: " + ecoresproductcategory.getCategory());
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecorescategoryattributegroup.class);
            Root<Ecorescategoryattributegroup> root = cq.from(Ecorescategoryattributegroup.class);
            Predicate category = criteriaBuilder.equal(root.get("ecoResCategoryRef"), ecoresproductcategory.getCategory());
            cq.select(root).where(category);
            return getEntityManager().createQuery(cq).getResultList();
        }catch(NoResultException e) {
            return null;
        }
    }
    public  List<Ecorescategoryattributegroup> findAttrGroupsByCategory(Product product){
        List<Ecorescategoryattributegroup> groups = new ArrayList<Ecorescategoryattributegroup>();
        for(Ecoresproductcategory ecoresproductcategory : this.findProductCategories(product)){
            for(Ecorescategoryattributegroup ecorescategoryattributegroup : this.findAttributeGroupsByCategory(ecoresproductcategory)){
                groups.add(ecorescategoryattributegroup);
            }
        }
        return groups;
    }

    public List<Ecoresgroupbyattribute> findProductsAttributesByCategory(Product product){
        List<Ecoresgroupbyattribute> attrRefList = new ArrayList<Ecoresgroupbyattribute>();
        for (Ecorescategoryattributegroup group : this.findAttrGroupsByCategory(product)){
            for(Ecoresgroupbyattribute ecoresgroupbyattribute : this.ecoresgroupbyattributeFacade.findAttributesByGroupRecId(group.getAttributeGroupRef())){
                attrRefList.add(ecoresgroupbyattribute);
            }
        }
        return attrRefList;
    }

    public List<Ecoresattribute> findProductsAttributes(Product product){
        List<Ecoresattribute> ecoresattributes = new ArrayList<Ecoresattribute>();
        for(Ecoresgroupbyattribute groupbyattribute : this.findProductsAttributesByCategory(product)){
            ecoresattributes.add(ecoresattributeFacade.find(groupbyattribute.getAttributeRef()));
        }
        return ecoresattributes;
    }

}
