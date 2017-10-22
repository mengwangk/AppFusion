package com.appfusion.web.rest.errors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @see <a href="http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-error-handling">Spring Doc</a>
 */
@ControllerAdvice
public class CloudVisionExceptionHandler extends ResponseEntityExceptionHandler {

    // Catch file size exceeded exception!
    @ExceptionHandler(MultipartException.class)	// Or create a custom exception
    @ResponseBody
    ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {

        HttpStatus status = getStatus(request);
        
        return new ResponseEntity<Object>(new CloudVisionError("0x000123",
                "Attachment size exceeds the allowable limit! (2MB)"), status);

        //  return new ResponseEntity(ex.getMessage(), status);

        // example
        // return new ResponseEntity("success", responseHeaders, HttpStatus.OK);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
