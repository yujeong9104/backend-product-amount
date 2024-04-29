package exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler({ProductRelatedException.class})
	protected ResponseEntity<?> handleProductException(ProductRelatedException e){
		return new ResponseEntity<>(e.getErrorCode().getMessage(),e.getErrorCode().getStatusCode());
	}
	
	 @ExceptionHandler({ Exception.class })
	    protected ResponseEntity<?> handleServerException(Exception ex) {
	        return new ResponseEntity<>("서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
	 }

}
