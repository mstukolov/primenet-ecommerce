package com.orders.facade;

import org.orders.entity.BonusSetup;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Stateless
public class BonusSetupFacade extends AbstractFacade<BonusSetup>{
    private static Logger _log = Logger.getLogger(BonusSetupFacade.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public BonusSetupFacade() {
        super(BonusSetup.class);
    }
}
