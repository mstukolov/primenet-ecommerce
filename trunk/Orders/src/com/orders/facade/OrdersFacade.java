package com.orders.facade;

import attributes.model.ProductAttributesvaluesView;
import org.orders.entity.BaseEntityAudit;
import org.orders.entity.Orders;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
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

        _log.info("Начинается поиск заказов по клиенту " + customer);
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Orders.class);
        Root<Orders> root = cq.from(Orders.class);

        Predicate cust = criteriaBuilder.equal(root.get("customer"), customer);
        cq.select(root).where(cust);

        return getEntityManager().createQuery(cq).getResultList();
    }
    public Double calcTotalSales(Date start){
        try {
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery<Tuple> cq = criteriaBuilder.createTupleQuery();
            Root<Orders> root = cq.from(Orders.class);

            Path<Date> dateCreatedPath  = root.get("created_at");

            Predicate startPeriod = criteriaBuilder.greaterThanOrEqualTo(dateCreatedPath, start);
            //Predicate endPeriod =
            Expression<Date> created = root.get("created_at");
            Expression<Double> amount = criteriaBuilder.sum(root.<Double>get("amount"));

            cq.multiselect(amount.alias("AMOUNT"), created);
            cq.where(criteriaBuilder.and(startPeriod));
            return (Double) getEntityManager().createQuery(cq).getSingleResult().get("AMOUNT");
        } catch(NoResultException e) {
            return null;
        }

    }
    public Double calcTotalSalesPeriod(Date start){
        try {
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Orders.class);
            Root<Orders> root = cq.from(Orders.class);

            Predicate startPeriod = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("processingAt"), start);

            Expression<Double> amount = criteriaBuilder.sum(root.<Double>get("amount"));

            cq.select(amount);
            cq.where(criteriaBuilder.and(startPeriod));
            return (Double) getEntityManager().createQuery(cq).getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
    public Integer calcTotalOrders(){
        try {
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Orders.class);
            Root<Orders> root = cq.from(Orders.class);
            //Expression<Double> qty = criteriaBuilder.equal(root.<Double>get("salesId"));
            //cq.select(qty);
            return (Integer) getEntityManager().createQuery(cq).getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

}
