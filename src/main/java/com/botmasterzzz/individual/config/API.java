package com.botmasterzzz.individual.config;

import com.botmasterzzz.individual.controller.Individual;
import com.botmasterzzz.individual.controller.IndividualController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/api")
public class API extends ResourceConfig {

    public static final String PARAM_USER = "A valid user object.";
    public static final String PARAM_FILE = "A valid file object.";
    public static final String PARAM_USER_ID = "A valid user id.";


    public static final String RESPONSE_MESSAGE_200 = "Successful.";
    public static final String RESPONSE_MESSAGE_202 = "Accepted.";
    public static final String RESPONSE_MESSAGE_400 = "Bad Request. The request was invalid or cannot be otherwise served. Requests without authentication are considered invalid and will yield this response.";
    public static final String RESPONSE_MESSAGE_401 = "Unauthorized. Authentication credentials were missing or incorrect.";
    public static final String RESPONSE_MESSAGE_403 = "Forbidden. The request is understood, but it has been refused or access is not allowed. ";
    public static final String RESPONSE_MESSAGE_503 = "Service Unavailable. The server is up, but the service is unavailable.";
    public static final String RESPONSE_MESSAGE_404 = "No content. Indicates nothing to be returned for this resource.";
    public static final String RESPONSE_MESSAGE_406 = "Not Acceptable. Indicates that the Media MIME Header Type supplied by the client is not supported by the server.";

    public static final int RESPONSE_200 = 200;
    public static final int RESPONSE_201 = 201;
    public static final int RESPONSE_202 = 202;
    public static final int RESPONSE_204 = 204;
    public static final int RESPONSE_400 = 400;
    public static final int RESPONSE_401 = 401;
    public static final int RESPONSE_403 = 403;
    public static final int RESPONSE_404 = 404;
    public static final int RESPONSE_406 = 406;
    public static final int RESPONSE_503 = 503;

    public API() {
        register(IndividualController.class);
    }
}
