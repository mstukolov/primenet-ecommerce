package attributes.core;

import attributes.model.Ecoresattributetype;
import attributes.model.Ecoresvalueenumeration;
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
public class EcoresvalueenumerationFacade extends AbstractFacade<Ecoresvalueenumeration> {
    private static Logger _log = Logger.getLogger(EcoresvalueenumerationFacade.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public EcoresvalueenumerationFacade() {
        super(Ecoresvalueenumeration.class);
    }
    public List<Ecoresvalueenumeration> findTypeEnumerationList(Ecoresattributetype ecoresattributetype){
        try{
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecoresvalueenumeration.class);
            Root<Ecoresvalueenumeration> root = cq.from(Ecoresvalueenumeration.class);
            Predicate attributetype = criteriaBuilder.equal(root.get("instanceRelationType"), ecoresattributetype.getRecid());
            cq.select(root).where(attributetype);
            return getEntityManager().createQuery(cq).getResultList();
        }catch(NoResultException e) {
            return null;
        }
    }
    public void create(Ecoresattributetype ecoresattributetype){
        _log.info("Создание значения списка: " + ecoresattributetype.getTypeName());
        Ecoresvalueenumeration ecoresvalueenumeration = new Ecoresvalueenumeration();
        ecoresvalueenumeration.setInstanceRelationType(ecoresattributetype.getRecid());
        super.create(ecoresvalueenumeration);
    }
    public void delete(Ecoresvalueenumeration ecoresvalueenumeration){
        super.remove(ecoresvalueenumeration);
    }
    public void save(Ecoresvalueenumeration ecoresvalueenumeration){
        super.edit(ecoresvalueenumeration);
    }
}
