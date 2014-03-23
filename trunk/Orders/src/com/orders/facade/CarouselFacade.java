package com.orders.facade;

import org.orders.entity.Carousel;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CarouselFacade extends AbstractFacade<Carousel>{

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public CarouselFacade() {
        super(Carousel.class);
    }
}
