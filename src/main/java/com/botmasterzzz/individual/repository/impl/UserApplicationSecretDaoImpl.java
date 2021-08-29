package com.botmasterzzz.individual.repository.impl;

import com.botmasterzzz.individual.entity.UserApplicationSecretEntity;
import com.botmasterzzz.individual.repository.UserApplicationSecretDao;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserApplicationSecretDaoImpl implements UserApplicationSecretDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void userApplicationSecretSave(UserApplicationSecretEntity userApplicationSecretEntity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(userApplicationSecretEntity);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void userApplicationSecretUpdate(UserApplicationSecretEntity userApplicationSecretEntity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(userApplicationSecretEntity);
        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings({"deprecation"})
    @Override
    public Optional<UserApplicationSecretEntity> userApplicationSecretGet(Long userId, String name) {
        UserApplicationSecretEntity userApplicationSecretEntity;
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(UserApplicationSecretEntity.class);
        criteria.add(Restrictions.eq("user.id", userId));
        criteria.add(Restrictions.eq("isDeleted", false));
        criteria.add(Restrictions.eq("isBanned", false));
        criteria.add(Restrictions.eq("name", name));
        criteria.setMaxResults(1);
        userApplicationSecretEntity = (UserApplicationSecretEntity) criteria.uniqueResult();
        session.close();
        return Optional.ofNullable(userApplicationSecretEntity);
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
