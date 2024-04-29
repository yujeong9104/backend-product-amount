package exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	
	PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 상품입니다."),
	
	PROMOTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 프로모션입니다."),
	DUPLICATE_PROMOTION(HttpStatus.BAD_REQUEST, "중복된 프로모션이 존재합니다."),
	INVALID_PROMOTION_PERIOD(HttpStatus.BAD_REQUEST, "프로모션의 사용기간이 유효하지 않습니다."),
	
	UNAPPLICABLE_PROMOTION(HttpStatus.BAD_REQUEST, "해당 상품에 적용이 불가능한 프로모션이 있습니다.");
    
	private final HttpStatus statusCode;
    private final String message;

}