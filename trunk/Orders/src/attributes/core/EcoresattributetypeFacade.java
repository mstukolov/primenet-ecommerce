package attributes.core;

import attributes.model.Ecoresattribute;
import attributes.model.Ecoresattributetype;
import com.orders.facade.AbstractFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.logging.Logger;

@Stateless
public class EcoresattributetypeFacade extends AbstractFacade<Ecoresattributetype> {
    private static Logger _log = Logger.getLogger(EcoresattributetypeFacade.class.getName());
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public EcoresattributetypeFacade() {
        super(Ecoresattributetype.class);
    }
    public Boolean isAttributeEnum(Ecoresattribute ecoresattribute){
        _log.info("Проверка атрибута на перечислимый тип: " +  ecoresattribute.getAttributeName());
        try{
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecoresattributetype.class);
            Root<Ecoresattributetype> root = cq.from(Ecoresattributetype.class);
            Predicate type = criteriaBuilder.equal(root.get("recid"), ecoresattribute.getAttributeTypeRef());
            cq.select(root).where(type);
            return ((Ecoresattributetype)getEntityManager().createQuery(cq).getSingleResult()).getEnumeration();
        }catch(NoResultException e) {
            return false;
        }
    }
    public String getAttributeDataType(Ecoresattribute ecoresattribute){
        _log.info("Код типа атрибута метода EcoresattributetypeFacade.getAttributeDataType: " +  ecoresattribute.getAttributeTypeRef());
        try{
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecoresattributetype.class);
            Root<Ecoresattributetype> root = cq.from(Ecoresattributetype.class);
            Predicate type = criteriaBuilder.equal(root.get("recid"), ecoresattribute.getAttributeTypeRef());
            cq.select(root).where(type);
            return ((Ecoresattributetype)getEntityManager().createQuery(cq).getSingleResult()).getDataType();
        }catch(NoResultException e) {
            return null;
        }
    }
    public Ecoresattributetype create(){
        Ecoresattributetype ecoresattributetype = new Ecoresattributetype();
        ecoresattributetype.setDataType("4350");
        ecoresattributetype.setCreatedBy("System");
        ecoresattributetype.setUpdatedBy("System");
        super.create(ecoresattributetype);
        return ecoresattributetype;
    }
    public void delete(Ecoresattributetype ecoresattributetype){
        super.remove(ecoresattributetype);
    }
    public void save(Ecoresattributetype ecoresattributetype){
            super.edit(ecoresattributetype);
    }

}
