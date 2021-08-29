package com.botmasterzzz.individual.repository;

import com.botmasterzzz.individual.entity.UserBookmarkEntity;

import java.util.List;
import java.util.Optional;

public interface UserBookmarkDao {

    void userBookmarkAdd(UserBookmarkEntity userBookmarkEntity);

    void userBookmarkUpdate(UserBookmarkEntity userBookmarkEntity);

    void userBookmarkRemove(UserBookmarkEntity userBookmarkEntity);

    Optional<UserBookmarkEntity> userBookmarkGet(Long userId, String apiUuid);

    List<UserBookmarkEntity> userBookmarkList(Long userId, int limit);

}
