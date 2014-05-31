package com.orders.reportmodel;


import com.orders.facade.AbstractFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Stateless
public class SalesLastMonthViewFacade extends AbstractFacade<GetsaleslastmonthView> {
    private static Logger _log = Logger.getLogger(SalesLastMonthViewFacade.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public SalesLastMonthViewFacade() {
        super(GetsaleslastmonthView.class);
    }
    public EntityManager getEntityManager() {
        return em;
    }
}
