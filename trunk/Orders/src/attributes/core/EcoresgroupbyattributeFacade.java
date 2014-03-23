package attributes.core;

import attributes.model.Ecoresattribute;
import attributes.model.Ecoresattributegroup;
import attributes.model.Ecoresgroupbyattribute;
import com.orders.facade.AbstractFacade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class EcoresgroupbyattributeFacade extends AbstractFacade<Ecoresgroupbyattribute> {
    private static Logger _log = Logger.getLogger(EcoresgroupbyattributeFacade.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    @EJB
    private EcoresattributeFacade attributeFacade;

    public EntityManager getEntityManager() {
        return em;
    }

    public EcoresgroupbyattributeFacade() {
        super(Ecoresgroupbyattribute.class);
    }
    public List<Ecoresgroupbyattribute> findAttributesByGroup(Ecoresattributegroup EcoResAttributeGroup){
        try{
            _log.info("Поиск атрибутов по группе" + EcoResAttributeGroup.getName());

            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecoresgroupbyattribute.class);
            Root<Ecoresgroupbyattribute> root = cq.from(Ecoresgroupbyattribute.class);
            Predicate group = criteriaBuilder.equal(root.get("attributeGroupRef"), EcoResAttributeGroup.getRecid());
            cq.select(root).where(group);
            return getEntityManager().createQuery(cq).getResultList();
        }catch(NoResultException e) {
            return null;
        }
    }
    public List<Ecoresgroupbyattribute> findAttributesByGroupRecId(Long recId){
        try{
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecoresgroupbyattribute.class);
            Root<Ecoresgroupbyattribute> root = cq.from(Ecoresgroupbyattribute.class);
            Predicate group = criteriaBuilder.equal(root.get("attributeGroupRef"), recId);
            cq.select(root).where(group);
            return getEntityManager().createQuery(cq).getResultList();
        }catch(NoResultException e) {
            return null;
        }
    }


    public Ecoresattribute findAttribute(Long ecoresattribute){
        return attributeFacade.find(ecoresattribute);
    }

    public void attachAttributesToGroup(Ecoresattribute[] ecoresattributes, Ecoresattributegroup ecoresattributegroup){
        _log.info("Привязка атрибутов к группе");
        for(Ecoresattribute ecoresattribute : ecoresattributes){

            if(! isAttributeRelatedGroup(ecoresattributegroup, ecoresattribute)){
                _log.info("Продукты не связаны");
                    Ecoresgroupbyattribute ecoresgroupbyattribute = new Ecoresgroupbyattribute();
                    ecoresgroupbyattribute.setAttributeGroupRef(ecoresattributegroup.getRecid());
                    ecoresgroupbyattribute.setAttributeRef(ecoresattribute.getRecid());
                    ecoresgroupbyattribute.setCreatedBy("Admin");
                    ecoresgroupbyattribute.setCreatedAt(new Date());
                    ecoresgroupbyattribute.setUpdatedBy("Admin");
                    this.create(ecoresgroupbyattribute);
                _log.info("Атрибуты были связаны:");
            } else {
                _log.info("Атрибуты уже связаны");
            }
        }

    }
    public Boolean isAttributeRelatedGroup(Ecoresattributegroup ecoresattributegroup, Ecoresattribute ecoresattribute) {
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecoresgroupbyattribute.class);
        Root<Ecoresgroupbyattribute> root = cq.from(Ecoresgroupbyattribute.class);
        Predicate group = criteriaBuilder.and(criteriaBuilder.equal(root.get("attributeGroupRef"), ecoresattributegroup.getRecid()),
                criteriaBuilder.equal(root.get("attributeRef"), ecoresattribute.getRecid()));
        cq.select(root).where(group);
        if(getEntityManager().createQuery(cq).getResultList().isEmpty()){
            return false;
        } else {return true;}
    }
}
