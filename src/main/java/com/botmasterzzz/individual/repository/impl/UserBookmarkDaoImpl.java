package com.botmasterzzz.individual.repository.impl;

import com.botmasterzzz.individual.entity.UserBookmarkEntity;
import com.botmasterzzz.individual.repository.UserBookmarkDao;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserBookmarkDaoImpl implements UserBookmarkDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Async
    @Override
    public void userBookmarkAdd(UserBookmarkEntity userBookmarkEntity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(userBookmarkEntity);
        session.getTransaction().commit();
        session.close();
    }

    @Async
    @Override
    public void userBookmarkUpdate(UserBookmarkEntity userBookmarkEntity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(userBookmarkEntity);
        session.getTransaction().commit();
        session.close();
    }

    @Async
    @Override
    public void userBookmarkRemove(UserBookmarkEntity userBookmarkEntity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(userBookmarkEntity);
        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings({"deprecation"})
    @Override
    public Optional<UserBookmarkEntity> userBookmarkGet(Long userId, String apiUuid) {
        UserBookmarkEntity userBookmarkEntity;
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(UserBookmarkEntity.class).createAlias("apiDataEntity", "api");
        criteria.add(Restrictions.eq("user.id", userId));
        criteria.add(Restrictions.eq("api.uuid", apiUuid));
        criteria.setMaxResults(1);
        userBookmarkEntity = (UserBookmarkEntity) criteria.uniqueResult();
        session.close();
        return Optional.ofNullable(userBookmarkEntity);
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    @Override
    public List<UserBookmarkEntity> userBookmarkList(Long userId, int limit) {
        List<UserBookmarkEntity> userBookmarkEntityList;
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(UserBookmarkEntity.class);
        criteria.addOrder(Order.desc("audWhenUpdate"));
        criteria.add(Restrictions.eq("user.id", userId));
        criteria.setMaxResults(limit);
        userBookmarkEntityList = criteria.list();
        session.close();
        return userBookmarkEntityList;
    }
}
