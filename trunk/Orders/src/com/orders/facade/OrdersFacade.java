package com.orders.facade;

import org.orders.entity.Orders;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class OrdersFacade extends AbstractFacade<Orders>{
    private static Logger _log = Logger.getLogger(OrdersFacade.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public OrdersFacade() {
        super(Orders.class);
    }

    public List<Orders> findCustOrders(String customer) {

        _log.info("Начинается поиск по клиенту " + customer);
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Orders.class);
        Root<Orders> root = cq.from(Orders.class);

        Predicate cust = criteriaBuilder.equal(root.get("customer"), "smb@yandex.ru");
        cq.select(root).where(cust);

        return getEntityManager().createQuery(cq).getResultList();
    }
}
