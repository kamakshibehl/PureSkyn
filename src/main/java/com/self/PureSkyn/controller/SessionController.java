package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.ApiResponse;
import com.self.PureSkyn.Model.ApiResponseStatus;
import com.self.PureSkyn.Model.Session;
import com.self.PureSkyn.exception.BadRequestException;
import com.self.PureSkyn.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/session")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Session>> setSession(@RequestBody Session session) {
        try {
            Session updateSession = sessionService.updateSession(session);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Session updated successfully", updateSession));
        } catch (BadRequestException e) {
            return ResponseEntity.ok(new ApiResponse<>(
                    ApiResponseStatus.FAIL,
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
        }


    }
}
