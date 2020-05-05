package de.mpg.mpdl.mpadmanager.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = HttpStatusCodeException.class)
    @ResponseBody
    public void jsonErrorHandler(HttpServletResponse response, HttpStatusCodeException e) throws Exception {
        e.printStackTrace();
        response.sendError(e.getStatusCode().value());
    }
}
