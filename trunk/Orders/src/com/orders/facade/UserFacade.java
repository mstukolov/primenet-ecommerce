package com.orders.facade;

import org.orders.entity.Proposal;
import org.orders.entity.UsersE;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


@Stateless
public class UserFacade extends AbstractFacade<UsersE>{
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(UsersE.class);
    }
    public UsersE findUserByLogin(String login) {
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(UsersE.class);
        Root<UsersE> root = cq.from(UsersE.class);
        Predicate user = criteriaBuilder.equal(root.get("login"), login);
        cq.select(root).where(user);
        return (UsersE) getEntityManager().createQuery(cq).getSingleResult();
    }
}
