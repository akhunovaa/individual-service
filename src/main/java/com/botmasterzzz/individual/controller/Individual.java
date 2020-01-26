package com.botmasterzzz.individual.controller;

import com.botmasterzzz.individual.common.constant.Constants;
import com.botmasterzzz.individual.config.API;
import com.botmasterzzz.individual.dto.UserDTO;
import com.botmasterzzz.individual.model.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ExecutionException;

public interface Individual {


    @GET
    @Path("/info")
    @ApiOperation(value = "Info", notes = "some notes regarding this method")
    @ApiResponses(value = {@ApiResponse(code = API.RESPONSE_202, message = API.RESPONSE_MESSAGE_202),
            @ApiResponse(code = API.RESPONSE_400, message = API.RESPONSE_MESSAGE_400),
            @ApiResponse(code = API.RESPONSE_401, message = API.RESPONSE_MESSAGE_401),
            @ApiResponse(code = API.RESPONSE_403, message = API.RESPONSE_MESSAGE_403),
            @ApiResponse(code = API.RESPONSE_404, message = API.RESPONSE_MESSAGE_404),
            @ApiResponse(code = API.RESPONSE_406, message = API.RESPONSE_MESSAGE_406),
            @ApiResponse(code = API.RESPONSE_503, message = API.RESPONSE_MESSAGE_503)})
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize("authenticated")
    Response userPasswordUpdate(@ApiParam(value = API.PARAM_USER_ID, required = true) @QueryParam(Constants.USER_ID) Long userId) throws ExecutionException, InterruptedException;

    @POST
    @Path("/password")
    @ApiOperation(value = "userPasswordUpdate", notes = "some notes regarding this method")
    @ApiResponses(value = {@ApiResponse(code = API.RESPONSE_202, message = API.RESPONSE_MESSAGE_202),
            @ApiResponse(code = API.RESPONSE_400, message = API.RESPONSE_MESSAGE_400),
            @ApiResponse(code = API.RESPONSE_401, message = API.RESPONSE_MESSAGE_401),
            @ApiResponse(code = API.RESPONSE_403, message = API.RESPONSE_MESSAGE_403),
            @ApiResponse(code = API.RESPONSE_404, message = API.RESPONSE_MESSAGE_404),
            @ApiResponse(code = API.RESPONSE_406, message = API.RESPONSE_MESSAGE_406),
            @ApiResponse(code = API.RESPONSE_503, message = API.RESPONSE_MESSAGE_503)})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize("authenticated")
    Response userPasswordUpdate(@ApiParam(value = API.PARAM_USER, required = true) @RequestBody UserDTO userDTO);
}
