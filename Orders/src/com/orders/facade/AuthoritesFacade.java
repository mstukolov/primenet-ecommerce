package com.orders.facade;

import org.orders.entity.Authorites;
import org.orders.entity.UsersE;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class AuthoritesFacade extends AbstractFacade<Authorites>{
    private static Logger _log = Logger.getLogger(AuthoritesFacade.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public AuthoritesFacade() {
        super(Authorites.class);
    }
    public List<Authorites> findAuthoritesByLogin(String login) {
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Authorites.class);
        Root<Authorites> root = cq.from(Authorites.class);
        Predicate user = criteriaBuilder.equal(root.get("login"), login);
        cq.select(root).where(user);
        return getEntityManager().createQuery(cq).getResultList();
    }
}
