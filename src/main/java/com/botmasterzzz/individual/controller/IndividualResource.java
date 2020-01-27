package com.botmasterzzz.individual.controller;

import com.botmasterzzz.individual.common.constant.Constants;
import com.botmasterzzz.individual.config.API;
import com.botmasterzzz.individual.model.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.*;

public interface IndividualResource {

    @GET
    @Path("/image/{userId}")
    @ApiOperation(value = "get image", notes = "some notes regarding this method")
    @ApiResponses(value = {@ApiResponse(code = API.RESPONSE_202, message = API.RESPONSE_MESSAGE_202),
            @ApiResponse(code = API.RESPONSE_400, message = API.RESPONSE_MESSAGE_400),
            @ApiResponse(code = API.RESPONSE_401, message = API.RESPONSE_MESSAGE_401),
            @ApiResponse(code = API.RESPONSE_403, message = API.RESPONSE_MESSAGE_403),
            @ApiResponse(code = API.RESPONSE_404, message = API.RESPONSE_MESSAGE_404),
            @ApiResponse(code = API.RESPONSE_406, message = API.RESPONSE_MESSAGE_406),
            @ApiResponse(code = API.RESPONSE_503, message = API.RESPONSE_MESSAGE_503)})
    @PreAuthorize("authenticated")
    public ResponseEntity<byte[]> userImageGet( @PathParam(Constants.USER_ID) int userId);

    @POST
    @Path("/image/upload")
    @ApiOperation(value = "upload image", notes = "some notes regarding this method")
    @ApiResponses(value = {@ApiResponse(code = API.RESPONSE_202, message = API.RESPONSE_MESSAGE_202),
            @ApiResponse(code = API.RESPONSE_400, message = API.RESPONSE_MESSAGE_400),
            @ApiResponse(code = API.RESPONSE_401, message = API.RESPONSE_MESSAGE_401),
            @ApiResponse(code = API.RESPONSE_403, message = API.RESPONSE_MESSAGE_403),
            @ApiResponse(code = API.RESPONSE_404, message = API.RESPONSE_MESSAGE_404),
            @ApiResponse(code = API.RESPONSE_406, message = API.RESPONSE_MESSAGE_406),
            @ApiResponse(code = API.RESPONSE_503, message = API.RESPONSE_MESSAGE_503)})
    @PreAuthorize("authenticated")
    public Response userImageUpload(@ApiParam(value = API.PARAM_FILE, required = true) @QueryParam(Constants.FILE) MultipartFile file);

}
