package com.botmasterzzz.individual.service;

import com.botmasterzzz.individual.dto.BookmarkDTO;

import java.util.List;

public interface UserBookmarkService {

    void createBookmark(Long userId, String apiUuid);

    void purgeBookmark(Long userId, String apiUuid);

    List<BookmarkDTO> getUserBookmarkList(Long userId, int limit);

}
