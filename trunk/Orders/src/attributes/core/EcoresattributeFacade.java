package attributes.core;

import attributes.model.Ecoresattribute;
import attributes.model.Ecoresattributegroup;
import com.orders.facade.AbstractFacade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EcoresattributeFacade extends AbstractFacade<Ecoresattribute> {
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;
    @EJB
    private EcoresattributetypeFacade ecoresattributetypeFacade;

    public EntityManager getEntityManager() {
        return em;
    }

    public EcoresattributeFacade() {
        super(Ecoresattribute.class);
    }

    public void createEcoresattribute(Ecoresattributegroup ecoresattributegroup){
        Ecoresattribute ecoresattribute = new Ecoresattribute();
        ecoresattribute.setAttributeName("Attribute");
        ecoresattribute.setAttributeTypeRef(ecoresattributetypeFacade.findAll().get(0).getRecid());
        ecoresattribute.setCreatedBy("System");
        ecoresattribute.setUpdatedBy("System");
        super.create(ecoresattribute);
    }
    public void delete(Ecoresattribute ecoresattribute){
        super.remove(ecoresattribute);
    }
    public void save(Ecoresattribute ecoresattribute){
        super.edit(ecoresattribute);
    }
}
