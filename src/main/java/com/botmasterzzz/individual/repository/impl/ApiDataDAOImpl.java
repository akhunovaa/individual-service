package com.botmasterzzz.individual.repository.impl;

import com.botmasterzzz.individual.entity.api.ApiDataEntity;
import com.botmasterzzz.individual.repository.ApiDataDao;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ApiDataDAOImpl implements ApiDataDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("deprecation")
    @Override
    public Optional<ApiDataEntity> getApiData(Long userId, String apiUuid) {
        ApiDataEntity apiDataEntity = null;
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(ApiDataEntity.class);
        criteria.add(Restrictions.eq("uuid", apiUuid));
        criteria.setMaxResults(1);
        try {
            apiDataEntity = (ApiDataEntity) criteria.uniqueResult();
        } catch (NonUniqueResultException e) {
            session.close();
        } finally {
            session.close();
        }
        return Optional.ofNullable(apiDataEntity);
    }

}
