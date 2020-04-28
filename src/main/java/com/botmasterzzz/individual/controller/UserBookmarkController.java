package com.botmasterzzz.individual.controller;

import com.botmasterzzz.individual.dto.BookmarkDTO;
import com.botmasterzzz.individual.dto.UserPrincipal;
import com.botmasterzzz.individual.model.Response;
import com.botmasterzzz.individual.service.UserBookmarkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/bookmark")
public class UserBookmarkController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBookmarkController.class);

    private static final int USER_BOOKMARK_LIMIT = 200;

    @Autowired
    private UserBookmarkService userBookmarkService;

    @PreAuthorize("authenticated")
    @RequestMapping(value = "/add", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response create(@RequestParam(name = "uuid") @Size(min = 3, max = 100) String uuid) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        Long userId = userPrincipal.getId();
        LOGGER.info("User bookmark create request came from login: {} and id: {} for api_uuid: {}", userPrincipal.getLogin(), userId, uuid);
        userBookmarkService.createBookmark(userId, uuid);
        BookmarkDTO bookmarkDTO = new BookmarkDTO(true);
        LOGGER.info("User bookmark create request done for login: {} and id: {} for api_uuid: {} \n bookmark: {}", userPrincipal.getLogin(), userPrincipal.getId(), uuid, bookmarkDTO);
        return getResponseDto(bookmarkDTO);
    }

    @PreAuthorize("authenticated")
    @RequestMapping(value = "/remove", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response remove(@RequestParam(name = "uuid") @Size(min = 3, max = 100) String uuid) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        Long userId = userPrincipal.getId();
        LOGGER.info("User bookmark remove request came from login: {} and id: {} for api_uuid: {}", userPrincipal.getLogin(), userPrincipal.getId(), uuid);
        userBookmarkService.purgeBookmark(userId, uuid);
        BookmarkDTO bookmarkDTO = new BookmarkDTO(false);
        LOGGER.info("User bookmark remove request done for login: {} and id: {} for api_uuid: {} \n bookmark: {}", userPrincipal.getLogin(), userId, uuid, bookmarkDTO);
        return getResponseDto(bookmarkDTO);
    }

    @PreAuthorize("authenticated")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response list(@RequestParam(name = "limit", required = false) @Min(1) @Max(USER_BOOKMARK_LIMIT) Integer limit) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        int requestedLimit = null == limit ? USER_BOOKMARK_LIMIT : limit;
        LOGGER.info("User bookmark list request came from login: {} and id: {}", userPrincipal.getLogin(), userPrincipal.getId());
        List<BookmarkDTO> userBookmarkList = userBookmarkService.getUserBookmarkList(userPrincipal.getId(), requestedLimit);
        LOGGER.info("User bookmark list request done for login: {} and id: {} \nbookmark: {}", userPrincipal.getLogin(), userPrincipal.getId(), userBookmarkList);
        return getResponseDto(userBookmarkList);
    }

}
