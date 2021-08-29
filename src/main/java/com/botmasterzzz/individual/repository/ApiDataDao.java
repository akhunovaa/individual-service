package com.botmasterzzz.individual.repository;

import com.botmasterzzz.individual.entity.api.ApiDataEntity;

import java.util.Optional;

public interface ApiDataDao {

    Optional<ApiDataEntity> getApiData(Long userId, String apiUuid);

}
