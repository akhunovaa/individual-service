package com.botmasterzzz.individual.controller;

import com.botmasterzzz.individual.dto.UserApplicationSecretDTO;
import com.botmasterzzz.individual.dto.UserPrincipal;
import com.botmasterzzz.individual.model.Response;
import com.botmasterzzz.individual.service.UserApplicationSecretService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/secret")
public class UserSecretController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSecretController.class);

    private static final int USER_APPLICATION_SECRET_KEY_LIMIT = 200;

    @Autowired
    private UserApplicationSecretService userApplicationSecretService;

    @PreAuthorize("authenticated")
    @RequestMapping(value = "/new", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response create(@RequestParam(name = "name", required = false) @Size(min = 3, max = 100) String name, @RequestParam(name = "limit", required = false) @Min(1) @Max(USER_APPLICATION_SECRET_KEY_LIMIT) Integer limit) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        int requestedLimit = null == limit ? USER_APPLICATION_SECRET_KEY_LIMIT : limit;
        String secretName = StringUtils.isEmpty(name) ? "default_name_" + new Date().getTime() : name;
        LOGGER.info("User secret create request came from login: {} and id: {}", userPrincipal.getLogin(), userPrincipal.getId());
        userApplicationSecretService.createNewUserApplicationSecret(userPrincipal.getId(), secretName);
        List<UserApplicationSecretDTO> userApplicationSecretDTOList = userApplicationSecretService.getUserApplicationSecretList(userPrincipal.getId(), requestedLimit);
        LOGGER.info("User secret create request done for login: {} and id: {} \nsecret_data: {}", userPrincipal.getLogin(), userPrincipal.getId(), userApplicationSecretDTOList);
        return getResponseDto(userApplicationSecretDTOList);
    }

    @PreAuthorize("authenticated")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response list(@RequestParam(name = "limit", required = false) @Min(1) @Max(USER_APPLICATION_SECRET_KEY_LIMIT) Integer limit) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        int requestedLimit = null == limit ? USER_APPLICATION_SECRET_KEY_LIMIT : limit;
        LOGGER.info("User secret list request came from login: {} and id: {}", userPrincipal.getLogin(), userPrincipal.getId());
        List<UserApplicationSecretDTO> userApplicationSecretDTOList = userApplicationSecretService.getUserApplicationSecretList(userPrincipal.getId(), requestedLimit);
        LOGGER.info("User secret list request done for login: {} and id: {} \nsecret_data: {}", userPrincipal.getLogin(), userPrincipal.getId(), userApplicationSecretDTOList);
        return getResponseDto(userApplicationSecretDTOList);
    }

}
