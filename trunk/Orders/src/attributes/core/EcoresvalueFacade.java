package attributes.core;

import attributes.model.Ecoresattribute;
import attributes.model.Ecoresvalue;
import com.orders.facade.AbstractFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.logging.Logger;

@Stateless
public class EcoresvalueFacade extends AbstractFacade<Ecoresvalue> {
    private static Logger _log = Logger.getLogger(EcoresvalueFacade.class.getName());
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public EcoresvalueFacade() {
        super(Ecoresvalue.class);
    }
    public Long createEcoresValue(Ecoresattribute ecoresattribute, String relationType){
        _log.info("Вызов метода EcoresvalueFacade.createEcoresValue: значение relationType = " + relationType);
        Ecoresvalue ecoresvalue = new Ecoresvalue();
        ecoresvalue.setRelationType(Long.valueOf(relationType));
        ecoresvalue.setBooleanValue(false);
        ecoresvalue.setDateTimeValue(new Date());
        ecoresvalue.setFloatValue(Float.valueOf("0"));
        ecoresvalue.setIntValue(0);
        ecoresvalue.setTextValue("");
        ecoresvalue.setUpdatedBy("System");
        ecoresvalue.setCreatedBy("System");
        super.create(ecoresvalue);
        _log.info("Код фактического значения атрибута продукта: " + ecoresvalue.getRecid());
        return ecoresvalue.getRecid();
    }
}
