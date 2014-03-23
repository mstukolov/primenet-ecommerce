package attributes.core;

import attributes.model.Ecoresattribute;
import attributes.model.Getattributeenumlist;
import com.orders.facade.AbstractFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class GetattributeenumlistFacade extends AbstractFacade<Getattributeenumlist> {
    private static Logger _log = Logger.getLogger(GetattributeenumlistFacade.class.getName());
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public GetattributeenumlistFacade() {
        super(Getattributeenumlist.class);
    }
    public List<Getattributeenumlist> findEnumsByAttributeId(Ecoresattribute ecoresattribute){
        _log.info("Код списка значений ENUM атрибута: " +  ecoresattribute.getAttributeName());
        try{
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Getattributeenumlist.class);
            Root<Getattributeenumlist> root = cq.from(Getattributeenumlist.class);
            Predicate attributeRecid = criteriaBuilder.equal(root.get("attributeRecid"), ecoresattribute.getRecid());
            cq.select(root).where(attributeRecid);
            return getEntityManager().createQuery(cq).getResultList();
        }catch(NoResultException e) {
            return null;
        }
    }
}
