package com.botmasterzzz.individual.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndividualController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndividualController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("authenticated")
    public Response userPasswordUpdate(@RequestBody UserDTO userDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        userDTO.setId(userPrincipal.getId());
        userService.userPasswordUpdate(userDTO);
        LOGGER.info("User password was updated to login: {}", userDTO.getLogin());
        return getResponseDto(userDTO);
    }


}