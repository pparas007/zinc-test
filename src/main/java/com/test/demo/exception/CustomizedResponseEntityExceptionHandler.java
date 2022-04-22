package com.test.demo.exception;

import java.util.Date;  
import org.springframework.http.HttpHeaders;  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.MethodArgumentNotValidException;  
import org.springframework.web.bind.annotation.ControllerAdvice;  
import org.springframework.web.bind.annotation.ExceptionHandler;  
import org.springframework.web.bind.annotation.RestController;  
import org.springframework.web.context.request.WebRequest;  
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice  
@RestController  
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler  
{  
	
	@ExceptionHandler(NonExistingAccountException.class)  
	public final ResponseEntity<Object> handleNonExistingAccountException(NonExistingAccountException ex, WebRequest request)  
	{  
		System.out.println("handleNonExistingAccountException");
		ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));  
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);  
	}  
	
	@ExceptionHandler(NotEnoughBalanceException.class)  
	public final ResponseEntity<Object> handleNotEnoughBalanceException(NotEnoughBalanceException ex, WebRequest request)  
	{  
		System.out.println("handleNotEnoughBalanceException");
		ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));  
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);  
	}
	
	@ExceptionHandler(CanNotDispeseTheGivenAmountException.class)  
	public final ResponseEntity<Object> handleCanNotDispeseTheGivenAmountException(CanNotDispeseTheGivenAmountException ex, WebRequest request)  
	{  
		System.out.println("handleCanNotDispeseTheGivenAmountException");
		ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));  
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);  
	}
	
	@ExceptionHandler(InvalidWithdrawalAmountException.class)  
	public final ResponseEntity<Object> handleInvalidWithdrawalAmountException(InvalidWithdrawalAmountException ex, WebRequest request)  
	{  
		System.out.println("handleNonExistingAccountException");
		ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));  
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);  
	} 
	
	@ExceptionHandler(PinNotMatchingException.class)  
	public final ResponseEntity<Object> handlePinNotMatchingException(PinNotMatchingException ex, WebRequest request)  
	{  
		System.out.println("handlePinNotMatchingException");
		ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));  
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);  
	}
	
	@ExceptionHandler(AccountNumberNotPresentException.class)  
	public final ResponseEntity<Object> handleAccountNumberNotPresentException(AccountNumberNotPresentException ex, WebRequest request)  
	{  
		System.out.println("handleAccountNumberNotPresentException");
		ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));  
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);  
	}
	
	@ExceptionHandler(PinNotEnteredException.class)  
	public final ResponseEntity<Object> handlePinNotEnteredException(PinNotEnteredException ex, WebRequest request)  
	{  
		System.out.println("handlePinNotEnteredException");
		ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));  
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);  
	}
	
	@Override  
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)   
	{  
		ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(), ex.getMessage(), ex.getBindingResult().toString());  
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);  
	} 
	
	@ExceptionHandler(Error.class)  
	public final ResponseEntity<Object> handleAllExceptions(Error ex, WebRequest request)  
	{  
		System.out.println("handleAllExceptions");
		ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(), "Some error happened. Please try again later.", request.getDescription(false));  
		return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);  
	} 
}  