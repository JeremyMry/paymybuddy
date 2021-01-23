package com.paymybuddy.app.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorsController implements ErrorController {

    private final Logger logger;

    public ErrorsController(Logger logger) {
        this.logger = logger;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                logger.error("This is a client error");
                return "404";
            }
            else if(statusCode == HttpStatus.BAD_REQUEST.value()) {
                logger.error("This is a request error");
                return "400";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                logger.error("This is a server error");
                return "500";
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
