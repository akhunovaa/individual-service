package com.botmasterzzz.individual.repository.impl;

import com.botmasterzzz.individual.entity.User;
import com.botmasterzzz.individual.repository.UserDao;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings({"deprecation"})
    public Optional<User> findByLogin(String login) {
        User user;
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("login", login));
        criteria.addOrder(Order.asc("audWhenCreate"));
        criteria.setMaxResults(1);
        user = (User) criteria.uniqueResult();
        session.close();
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        Session session = sessionFactory.openSession();
        Transaction updateTransaction = session.beginTransaction();
        User user = session.get(User.class, id);
        updateTransaction.commit();
        session.close();
        return Optional.ofNullable(user);
    }

    @Override
    @SuppressWarnings("deprecation")
    public Boolean existsByLogin(String login) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("login", login));
        criteria.setProjection(Projections.id());
        boolean exists;
        try {
            exists = criteria.uniqueResult() != null;
        } catch (NonUniqueResultException e) {
            session.close();
            return true;
        } finally {
            session.close();
        }
        return exists;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Boolean existsByEmail(String email) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("email", email));
        criteria.setProjection(Projections.id());
        boolean exists;
        try {
            exists = criteria.uniqueResult() != null;
        } catch (NonUniqueResultException e) {
            session.close();
            return true;
        } finally {
            session.close();
        }
        return exists;
    }

    @Async
    @Override
    public void userUpdate(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();
        session.close();
    }
}