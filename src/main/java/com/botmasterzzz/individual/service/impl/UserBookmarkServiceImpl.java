package com.botmasterzzz.individual.service.impl;

import com.botmasterzzz.individual.dto.BookmarkDTO;
import com.botmasterzzz.individual.entity.User;
import com.botmasterzzz.individual.entity.UserBookmarkEntity;
import com.botmasterzzz.individual.entity.api.ApiDataEntity;
import com.botmasterzzz.individual.exception.ApiDataNotFoundException;
import com.botmasterzzz.individual.repository.ApiDataDao;
import com.botmasterzzz.individual.repository.UserBookmarkDao;
import com.botmasterzzz.individual.repository.UserDao;
import com.botmasterzzz.individual.service.UserBookmarkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserBookmarkServiceImpl implements UserBookmarkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBookmarkService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserBookmarkDao userBookmarkDao;

    @Autowired
    private ApiDataDao apiDataDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "user-api-data", key = "#userId"),
            @CacheEvict(value = "user-bookmark-list", key = "#userId")
    })
    public void createBookmark(Long userId, String apiUuid) {
        User user = userDao.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        ApiDataEntity apiDataEntity = apiDataDao.getApiData(userId, apiUuid).orElseThrow(() -> new ApiDataNotFoundException("API not found"));
        LOGGER.info("Request for a bookmark create to user: {}", writeValueAsString(user));
        UserBookmarkEntity userBookmarkEntity;
        Optional<UserBookmarkEntity> userBookmarkEntityOptional = userBookmarkDao.userBookmarkGet(userId, apiUuid);
        if (userBookmarkEntityOptional.isPresent()) {
            userBookmarkEntity = userBookmarkEntityOptional.get();
            userBookmarkEntity.setNote("Re-updated from user");
            userBookmarkEntity.setName(apiUuid);
            userBookmarkDao.userBookmarkUpdate(userBookmarkEntity);
        } else {
            userBookmarkEntity = new UserBookmarkEntity();
            userBookmarkEntity.setApiDataEntity(apiDataEntity);
            userBookmarkEntity.setUser(user);
            userBookmarkDao.userBookmarkAdd(userBookmarkEntity);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "user-api-data", key = "#userId"),
            @CacheEvict(value = "user-bookmark-list", key = "#userId")
    })
    public void purgeBookmark(Long userId, String apiUuid) {
        User user = userDao.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        LOGGER.info("Request for a bookmark remove to user: {}", writeValueAsString(user));
        UserBookmarkEntity userBookmarkEntity;
        Optional<UserBookmarkEntity> userBookmarkEntityOptional = userBookmarkDao.userBookmarkGet(userId, apiUuid);
        if (userBookmarkEntityOptional.isPresent()) {
            userBookmarkEntity = userBookmarkEntityOptional.get();
            userBookmarkDao.userBookmarkRemove(userBookmarkEntity);
        }
    }

    @Override
    @Cacheable(value = "user-bookmark-list", key = "#userId")
    public List<BookmarkDTO> getUserBookmarkList(Long userId, int limit) {
        List<BookmarkDTO> bookmarkDTOList = new ArrayList<>();

        List<UserBookmarkEntity> bookmarkEntityList = userBookmarkDao.userBookmarkList(userId, limit);
        for (UserBookmarkEntity userBookmarkEntity : bookmarkEntityList) {
            BookmarkDTO bookmarkDTO = new BookmarkDTO();
            bookmarkDTO.setChecked(true);
            bookmarkDTO.setCreatedTimestamp(userBookmarkEntity.getAudWhenCreate().getTime());
            bookmarkDTO.setUpdatedTimestamp(userBookmarkEntity.getAudWhenUpdate().getTime());
            bookmarkDTO.setName(userBookmarkEntity.getApiDataEntity().getUuid());
            bookmarkDTOList.add(bookmarkDTO);
        }

        return bookmarkDTOList;
    }


    private String writeValueAsString(User user) {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Writing value to JSON failed: " + user.toString());
        }
    }
}
