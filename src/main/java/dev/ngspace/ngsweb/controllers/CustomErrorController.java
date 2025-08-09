package dev.ngspace.ngsweb.controllers;

import java.io.IOException;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    protected final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Throwable error = errorAttributes.getError(servletWebRequest);
        
        if (error instanceof IOException) {
        	NGSWebController.logger.error("404 error");
        	return new ModelAndView("404");
        }

    	NGSWebController.logger.error("Generic error");
        return new ModelAndView("generic_error");
    }
}