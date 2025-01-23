package com.kh.secom.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice // 예외처리를 위한 클래스 애노테이션
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(InvalidParameterException.class)
	public ResponseEntity<?> handleInvalidParameter(InvalidParameterException e){
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	@ExceptionHandler(DuplicateUserException.class)
	public ResponseEntity<?> handleDuplicateUser(DuplicateUserException e){
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	

}
