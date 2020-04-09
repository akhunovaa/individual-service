package com.botmasterzzz.individual.controller;

import com.botmasterzzz.individual.dto.UserProfileDTO;
import com.botmasterzzz.individual.model.Response;
import com.botmasterzzz.individual.service.UserService;
import com.botmasterzzz.individual.util.ClientInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserProfileController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "{nickname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response userInfo(@PathVariable(name = "nickname") String nickname, HttpServletRequest httpServletRequest) {
        String clientBrowser = ClientInfoUtil.getClientBrowser(httpServletRequest);
        String clientOs = ClientInfoUtil.getClientOS(httpServletRequest);
        String clientIp = ClientInfoUtil.getClientIpAddr(httpServletRequest);
        String xRealIp = httpServletRequest.getHeader("X-Real-IP");
        String xForwardedFor = httpServletRequest.getHeader("X-Forwarded-For");
        String host = httpServletRequest.getHeader("Host");
        String xForwardedProto = httpServletRequest.getHeader("X-Forwarded-Proto");
        LOGGER.info("User info invoking request came for nickname: {} \n{} \n {} \n {} \n {} \n {} \n {} \n {}", nickname, clientBrowser, clientOs, clientIp, xRealIp, xForwardedFor, host, xForwardedProto);
        UserProfileDTO userProfileDTO = userService.findUserProfile(nickname);
        LOGGER.info("User info invoking request done for nickname: {} \n {} \n{} \n {} \n {} \n {} \n {} \n {} \n {}", nickname, userProfileDTO, clientBrowser, clientOs, clientIp, xRealIp, xForwardedFor, host, xForwardedProto);
        return getResponseDto(userProfileDTO);
    }

}
