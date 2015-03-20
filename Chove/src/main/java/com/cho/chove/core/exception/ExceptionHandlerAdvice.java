package com.cho.chove.core.exception;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception Handler
 * @author ChoHoyoung
 * @since 2015.03.20
 * @version 0.1
 * @see
 *
 * <pre>
 * << Modification Information >>
 *   
 *  UpdateDate  Updator     Description
 *  ----------  ----------  ---------------------------
 *  2014.03.20  ChoHoyoung  initialize
 *
 * </pre>
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
    
	/**
	 * SQLException Handler
	 * @param request
	 * @param ex
	 */
    @ExceptionHandler(SQLException.class)
    public void handleSQLException(HttpServletRequest request, Exception ex) {
        logger.info("SQLException Occured:: URL="+request.getRequestURL());
    }
     
    /**
     * IOException Handler
     */
    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="IOException occured")
    @ExceptionHandler(IOException.class)
    public void handleIOException() {
        logger.error("IOException handler executed");
    }
    
    /**
     * All Exception Handler
     * @param exception
     */
    @ExceptionHandler(value = Exception.class)
    public void exception(Exception exception) {
    	logger.error(exception.toString());
    }
}