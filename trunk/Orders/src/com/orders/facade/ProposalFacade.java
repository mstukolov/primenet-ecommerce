package com.orders.facade;

import org.orders.entity.Proposal;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class ProposalFacade extends AbstractFacade<Proposal>{
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public ProposalFacade() {
        super(Proposal.class);
    }
    public List<Proposal> findPropolsalsByProduct(Long recid) {
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Proposal.class);
        Root<Proposal> root = cq.from(Proposal.class);
        Predicate product = criteriaBuilder.equal(root.get("product"), recid);
        cq.select(root).where(product);
        return getEntityManager().createQuery(cq).getResultList();
    }
}
