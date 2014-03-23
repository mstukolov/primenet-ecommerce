package attributes.core;

import attributes.model.Ecoresattributegroup;
import attributes.model.Ecorescategoryattributegroup;
import com.orders.facade.AbstractFacade;
import org.orders.entity.Ecorescategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

@Stateless

public class EcorescategoryattributegroupFacade extends AbstractFacade<Ecorescategoryattributegroup> {
    private static Logger _log = Logger.getLogger(EcorescategoryattributegroupFacade.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public EcorescategoryattributegroupFacade() {
        super(Ecorescategoryattributegroup.class);
    }
    public void create(Ecoresattributegroup ecoresattributegroup, Ecorescategory ecorescategory){
        Ecorescategoryattributegroup ecorescategoryattributegroup = new Ecorescategoryattributegroup();
        ecorescategoryattributegroup.setAttributeGroupRef(ecoresattributegroup.getRecid());
        ecorescategoryattributegroup.setEcoResCategoryRef(ecorescategory.getRecid());
        ecorescategoryattributegroup.setCreatedBy("System");
        ecorescategoryattributegroup.setUpdatedBy("System");
        super.create(ecorescategoryattributegroup);
    }
    public void delete(Ecorescategoryattributegroup ecorescategoryattributegroup){
        super.remove(ecorescategoryattributegroup);
    }
    public List<Ecorescategoryattributegroup> findAttributeGroupsByCategory(Ecorescategory ecorescategory){
        try{
            _log.info("Поиск групп атрибутов по категории: " + ecorescategory.getName());
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecorescategoryattributegroup.class);
            Root<Ecorescategoryattributegroup> root = cq.from(Ecorescategoryattributegroup.class);
            Predicate category = criteriaBuilder.equal(root.get("ecoResCategoryRef"), ecorescategory.getRecid());
            cq.select(root).where(category);
            return getEntityManager().createQuery(cq).getResultList();
        }catch(NoResultException e) {
            return null;
        }
    }
}
