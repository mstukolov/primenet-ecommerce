package attributes.core;

import attributes.model.Ecoresattributegroup;
import com.orders.facade.AbstractFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EcoresattributegroupFacade extends AbstractFacade<Ecoresattributegroup> {
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public EcoresattributegroupFacade() {
        super(Ecoresattributegroup.class);
    }

    public void create(){
        Ecoresattributegroup ecoresattributegroup = new Ecoresattributegroup();
        ecoresattributegroup.setName("Group");
        super.create(ecoresattributegroup);
    }
    public void delete(Ecoresattributegroup ecoresattributegroup){
        super.remove(ecoresattributegroup);
    }
    public void save(Ecoresattributegroup ecoresattributegroup){
        super.edit(ecoresattributegroup);
    }
}
