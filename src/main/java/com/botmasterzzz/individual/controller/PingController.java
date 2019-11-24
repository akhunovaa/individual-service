package com.botmasterzzz.individual.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class PingController extends AbstractController{

    private static final Logger LOGGER = LoggerFactory.getLogger(PingController.class);

    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void mobileServicePing(HttpServletResponse httpServletResponse) {
        returnJsonString("The command was successful", httpServletResponse);
    }

}
