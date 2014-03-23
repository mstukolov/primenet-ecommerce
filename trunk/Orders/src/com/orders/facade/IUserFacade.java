package com.orders.facade;

import org.orders.entity.UsersE;

import javax.ejb.Remote;
import javax.persistence.EntityManager;
import java.util.List;

@Remote
public interface IUserFacade {
    public void create(UsersE entity);
    public void edit(UsersE entity);
    public void remove(UsersE entity);
    public UsersE find(Object id);
    public List<UsersE> findAll();
    public List findRange(int[] range);
    public int count();
    public EntityManager getEntityManager();

}
