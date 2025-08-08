package dev.ngspace.ngsweb;

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

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Throwable error = this.errorAttributes.getError(servletWebRequest);
        
        if (error instanceof IOException) {
        	return new ModelAndView("404");
        }
        
        return new ModelAndView("generic_error");
    }

    // Optional (Spring Boot 2.3+ no longer requires this method)
    public String getErrorPath() {
        return "/error";
    }
}