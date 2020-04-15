package com.botmasterzzz.individual.repository.impl;

import com.botmasterzzz.individual.entity.UserApplicationSecretEntity;
import com.botmasterzzz.individual.repository.UserApplicationSecretDao;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserApplicationSecretDaoImpl implements UserApplicationSecretDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Async
    @Override
    public void userApplicationSecretSave(UserApplicationSecretEntity userApplicationSecretEntity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(userApplicationSecretEntity);
        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    @Override
    public List<UserApplicationSecretEntity> userApplicationSecretList(Long userId, int limit) {
        List<UserApplicationSecretEntity> userApplicationSecretEntityList;
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(UserApplicationSecretEntity.class);
        criteria.addOrder(Order.desc("audWhenCreate"));
        criteria.add(Restrictions.eq("user.id", userId));
        criteria.add(Restrictions.eq("isDeleted", false));
        criteria.add(Restrictions.eq("isBanned", false));
        criteria.setMaxResults(limit);
        userApplicationSecretEntityList = criteria.list();
        session.close();
        return userApplicationSecretEntityList;
    }
}
