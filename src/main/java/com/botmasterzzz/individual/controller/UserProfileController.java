package com.botmasterzzz.individual.controller;

import com.botmasterzzz.individual.dto.UserPrincipal;
import com.botmasterzzz.individual.dto.UserProfileDTO;
import com.botmasterzzz.individual.model.Response;
import com.botmasterzzz.individual.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserProfileController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "{nickname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("authenticated")
    public Response userInfo(@PathVariable(name = "nickname") String nickname) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        LOGGER.info("User info invoking request came from login: {} and id: {} requested username: {}", userPrincipal.getLogin(), userPrincipal.getId(), nickname);
        UserProfileDTO userProfileDTO = userService.findUserProfile(nickname);
        LOGGER.info("User info data came with a name: {} and id: {} requested username: {}", userProfileDTO.getName(), userProfileDTO.getId(), nickname);
        return getResponseDto(userProfileDTO);
    }

}
