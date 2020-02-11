package com.botmasterzzz.individual.controller;

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

import java.util.concurrent.ExecutionException;

@RestController
public class IndividualController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndividualController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("authenticated")
    public Response userPasswordUpdate(@RequestParam(name = "id") Long userId) throws ExecutionException, InterruptedException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        LOGGER.info("User info invoking request came from login: {} and id: {}", userPrincipal.getLogin(), userPrincipal.getId());
        UserDTO userDTO = userService.findUser(userId);
        LOGGER.info("User info data came with a login: {} and id: {}", userDTO.getLogin(), userDTO.getId());
        return getResponseDto(userDTO);
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("authenticated")
    public Response userPasswordUpdate(@RequestBody PasswordDTO passwordDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        passwordDTO.setId(userPrincipal.getId());
        userService.userPasswordUpdate(passwordDTO);
        LOGGER.info("User password was updated to login: {}", userPrincipal.getLogin());
        return getResponseDto(passwordDTO);
    }


}
