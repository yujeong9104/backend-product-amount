package antigravity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	@ExceptionHandler(ProductRelatedException.class)
	protected ResponseEntity<?> handleProductException(ProductRelatedException e){
		
		log.error("Product related exception occurred: {}", e.getErrorCode().getMessage());
		
		return new ResponseEntity<>(e.getErrorCode().getMessage(),e.getErrorCode().getStatusCode());
	}
	
	 @ExceptionHandler( Exception.class )
	    protected ResponseEntity<?> handleServerException(Exception ex) {
	        return new ResponseEntity<>("서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
	 }

}
