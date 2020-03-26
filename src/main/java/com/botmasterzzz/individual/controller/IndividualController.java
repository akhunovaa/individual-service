package com.botmasterzzz.individual.controller;

import com.botmasterzzz.individual.dto.IndividualDTO;
import com.botmasterzzz.individual.dto.PasswordDTO;
import com.botmasterzzz.individual.dto.UserDTO;
import com.botmasterzzz.individual.dto.UserPrincipal;
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

import javax.validation.Valid;

@RestController
@SuppressWarnings("deprecation")
public class IndividualController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndividualController.class);

    @Autowired
    private UserService userService;

    @PreAuthorize("authenticated")
    @RequestMapping(value = "/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response getCurrentUser() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        long userId = userPrincipal.getId();
        IndividualDTO individualDTO = userService.findIndividual(userId);
        return getResponseDto(individualDTO);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("authenticated")
    public Response userInfo(@RequestParam(name = "id") Long userId) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        LOGGER.info("User info invoking request came from login: {} and id: {}", userPrincipal.getLogin(), userPrincipal.getId());
        UserDTO userDTO = userService.findUser(userId);
        LOGGER.info("User info data came with a login: {} and id: {}", userDTO.getLogin(), userDTO.getId());
        return getResponseDto(userDTO);
    }

    @RequestMapping(value = "/password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("authenticated")
    public Response userPasswordUpdate(@Valid @RequestBody PasswordDTO passwordDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        passwordDTO.setId(userPrincipal.getId());
        userService.userPasswordUpdate(passwordDTO);
        LOGGER.info("User password was updated to user id: {}", userPrincipal.getId());
        return getResponseDto(passwordDTO);
    }


    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("authenticated")
    public Response userInfoUpdate(@Valid @RequestBody IndividualDTO individualDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        individualDTO.setId(userPrincipal.getId());
        LOGGER.info("User information update to user id: {}", userPrincipal.getId());
        userService.individualUpdate(individualDTO);
        return getResponseDto(individualDTO);
    }

}
