package com.accenture.covid19.controller;

import com.accenture.covid19.dto.SimpleStringResponse;
import com.accenture.covid19.dto.User;
import com.accenture.covid19.exception.ErrorResponse;
import com.accenture.covid19.service.RegisterServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.accenture.covid19.service.RegisterServiceImpl.ERROR_MSG;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/rest")
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE, tags = {AccentureController.REGISTRATION_TAG})
public class AccentureController {

    public static final String REGISTRATION_TAG = "Registration";

    private final RegisterServiceImpl registerService;

    public AccentureController(RegisterServiceImpl registerService) {
        this.registerService = registerService;
    }


    @ApiOperation(tags = REGISTRATION_TAG, value = "Book a day for visiting the office")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SimpleStringResponse.class),
            @ApiResponse(code = 409, message = "The user already booked for this day", response = ErrorResponse.class),
            @ApiResponse(code = 412, message = "The user_id and the date are required",
                    responseHeaders = @ResponseHeader(name = ERROR_MSG, description = "if user_id or date is missing"))
    })
    @PutMapping(value = "/register", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@ApiParam(name = "user", required = true) @RequestBody User user) {
        ResponseEntity<?> responseEntity = preConditionalValidator(user);
        if (nonNull(responseEntity)) {
            return responseEntity;
        }
        return ResponseEntity.ok(registerService.save(user));
    }

    private ResponseEntity<?> preConditionalValidator(User user) {
        if (isBlank(user.getUserId())) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .header(ERROR_MSG, "user_id is required")
                    .build();
        }
        if (isNull(user.getDate())) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .header(ERROR_MSG, "date is required")
                    .build();
        }
        return null;
    }

    @ApiOperation(tags = REGISTRATION_TAG, value = "Get user'sposition on the waiting-list (always on the actual day)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SimpleStringResponse.class),
            @ApiResponse(code = 409, message = "The user already booked for this day", response = ErrorResponse.class
            ),
    })
    @GetMapping(value = "/status/{userId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> status(@ApiParam(name = "userId", required = true) @PathVariable String userId) {
        return ResponseEntity.ok(registerService.getStatus(userId));
    }

    @ApiOperation(tags = REGISTRATION_TAG,
            value = "Get user'sposition on the waiting-list (always on the actual day)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SimpleStringResponse.class),
            @ApiResponse(code = 409, message = "The user already booked for this day", response = ErrorResponse.class)
    })
    @GetMapping(value = "/entry/{userId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> entry(@ApiParam(name = "userId", required = true) @PathVariable("userId") String userId) {
        return ResponseEntity.ok(registerService.checkIn(userId));
    }

    @ApiOperation(tags = REGISTRATION_TAG, value = "Get user'sposition on the waiting-list (always on the actual day)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SimpleStringResponse.class),
            @ApiResponse(code = 404, message = "Can't find the last checkin", response = ErrorResponse.class)
    })
    @GetMapping(value = "/exit/{userId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> exit(@ApiParam(name = "userDTO", required = true) @PathVariable("userId") String userId) {
        registerService.checkOut(userId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(tags = REGISTRATION_TAG, value = "Get a office book by the given user and day.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 409, message = "The user already booked for this day", response = ErrorResponse.class),
            @ApiResponse(code = 412, message = "The user_id and date is required",
                    responseHeaders = @ResponseHeader(name = ERROR_MSG, description = "if user_id or date is missing"))
    })
    @DeleteMapping(value = "/register/delete", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteRegister(@ApiParam(name = "userDTO", required = true) @RequestBody User user) {
        ResponseEntity<?> responseEntity = preConditionalValidator(user);
        if (nonNull(responseEntity)) {
            return responseEntity;
        }
        registerService.deleteBook(user);
        return ResponseEntity.ok().build();
    }

}
