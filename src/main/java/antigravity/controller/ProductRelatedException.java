package antigravity.controller;

import antigravity.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductRelatedException extends RuntimeException{
	

	private final ErrorCode errorCode;
	
}
