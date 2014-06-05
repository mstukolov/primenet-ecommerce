package com.orders.facade;

import org.orders.entity.Logevents;
import org.orders.entity.Proposal;
import org.orders.entity.UsersE;
import org.springframework.security.core.userdetails.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


@Stateless
public class UserFacade extends AbstractFacade<UsersE>{
    private static Logger _log = Logger.getLogger(UserFacade.class.getName());

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
    public void logUserEvents(User user, String action, Date date, String ipAddress){

        Logevents event = new Logevents();
        event.setAction(action);
        event.setUser(user.getUsername());
        event.setActiondate(date);
        event.setIpaddress(ipAddress);
        em.getEntityManagerFactory().createEntityManager().persist(event);


        _log.info("Запись в базу данных о действиях пользователя= "
                            + user.getUsername()
                 + " :Дейстиве: " + action
                 + " :Дата:"  + date
                 + " :ipAddress: " + ipAddress
         );
    }
}
