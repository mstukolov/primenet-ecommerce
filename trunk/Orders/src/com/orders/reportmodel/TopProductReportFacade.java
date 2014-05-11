package com.orders.reportmodel;

import com.orders.facade.AbstractFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Stateless
public class TopProductReportFacade extends AbstractFacade<GettopproductEntity> {
    private static Logger _log = Logger.getLogger(GettopproductEntity.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public TopProductReportFacade() {
        super(GettopproductEntity.class);
    }
    public EntityManager getEntityManager() {
        return em;
    }
}
