package antigravity.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import antigravity.controller.ProductRelatedException;
import antigravity.domain.entity.Promotion;
import antigravity.enums.ErrorCode;
import antigravity.repository.PromotionRepository;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {
	
	@InjectMocks
	private PromotionService promotionService;
	
	@Mock
	private PromotionRepository promotionRepository;
	
    @Test
    @DisplayName("중복된 프로모션이 존재하는가 검사 - 성공")
    void productNotFoundTestCompleted() {
    	
    	 //given
    	 int[] couponIds = {1, 2, 3};
    	 
    	 //when, then
    	 assertDoesNotThrow(() -> promotionService.isDuplicatedPromotion(couponIds));

    }

    @Test
    @DisplayName("중복된 프로모션이 존재하는가 검사 - 오류")
    void productNotFoundTest() {
    	
    	//given
    	int[] couponIds = {1, 2, 3, 3};
    	
    	//when
    	ProductRelatedException e = assertThrows(ProductRelatedException.class, () -> {
    		promotionService.isDuplicatedPromotion(couponIds);
    	});
    	
    	//then
    	assertEquals(ErrorCode.DUPLICATE_PROMOTION,e.getErrorCode());
    }
    
    @Test
    @DisplayName("존재하지 않는 프로모션이 하나라도 있는가 검사 - 성공")
    void isPromotionNotFoundTestCompleted() {
        
    	//given
    	int[] couponIds = {1, 2}; 
    	
    	//when
        List<Promotion> promotionList = List.of(
                new Promotion(1,"쿠폰", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1),null),
                new Promotion(2,"코드", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );
        
        //then
        assertDoesNotThrow(() -> promotionService.isPromotionNotFound(couponIds, promotionList));

    }
        
    
    @Test
    @DisplayName("존재하지 않는 프로모션이 하나라도 있는가 검사 - 오류")
    void isPromotionNotFoundTest() {
        
    	//given 
    	int[] couponIds = {1, 2, 3}; // 존재하지 않는 프로모션 ID가 있는 경우
    	
        List<Promotion> promotionList = List.of(
                new Promotion(1,"쿠폰", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1),null),
                new Promotion(2,"코드", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );
        
        //when
        ProductRelatedException exception = assertThrows(ProductRelatedException.class, () -> {
            promotionService.isPromotionNotFound(couponIds, promotionList);
        });
        
        //then
        assertEquals(ErrorCode.PROMOTION_NOT_FOUND, exception.getErrorCode());
    }
    
    @Test
    @DisplayName("프모로션 유효성 검사 - 성공")
    void testIsInvalidPromotionTestCompleted() {
    	
    	//given 
        List<Promotion> promotionList = List.of(
        		new Promotion(1,"COUPON", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1), null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1), null)
        );
        
        //when, then
        assertDoesNotThrow(() -> promotionService.isInvailedPomotion(promotionList));

    }
    
    @Test
    @DisplayName("프모로션의 유효기간이 맞는가 검사 - 오류")
    void testIsInvalidPromotionTestPeriod() {
    	
    	//given
        List<Promotion> promotionList = List.of(
        		// 유효하지 않는 프로모션 기간 삽입
        		new Promotion(1,"COUPON", "프로모션1", "WON", 1000, LocalDate.now().plusDays(1), LocalDate.now(),null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );
        
        //when
        ProductRelatedException exception = assertThrows(ProductRelatedException.class, () -> {
            promotionService.isInvailedPomotion(promotionList);
        });
        
        //then
        assertEquals(ErrorCode.INVALID_PROMOTION_PERIOD, exception.getErrorCode());
    }
    
    @Test
    @DisplayName("프모로션 타입과 할인타입이 일치하는가 검사 - 오류")
    void testIsInvalidPromotiontTestTypeMatch() {
    	
    	//given
        List<Promotion> promotionList = List.of(
        		//프로모션 타입과 할인타입이 일치 하지 않은 프로모션 삽입
        		new Promotion(1,"COUPON", "프로모션1", "PERCENT", 10, LocalDate.now(), LocalDate.now().plusDays(1),null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );
        
        //when
        ProductRelatedException exception = assertThrows(ProductRelatedException.class, () -> {
            promotionService.isInvailedPomotion(promotionList);
        });
        
        //then
        assertEquals(ErrorCode.PROMOTION_DISCOUNT_TYPE_MISMATCH, exception.getErrorCode());
    }
    
    @Test
    @DisplayName("프모로션 PERCENT 값이 유효한가 검사 - 오류")
    void testIsInvalidPromotiontTestPercent() {
    	
    	//given
        List<Promotion> promotionList = List.of(
        		// 유효하지 않는 PERCENT값 삽입
        		new Promotion(1,"COUPON", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1),null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 200, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );
        
        //when
        ProductRelatedException exception = assertThrows(ProductRelatedException.class, () -> {
            promotionService.isInvailedPomotion(promotionList);
        });
        
        //then
        assertEquals(ErrorCode.INVALID_PROMOTION_VALUE, exception.getErrorCode());
    }
    
    @Test
    @DisplayName("프모로션 타입이 유효한가 검사 - 오류")
    void testIsInvalidPromotiontTestType() {
        
    	//given
    	List<Promotion> promotionList = List.of(
        		// 유효하지 프로모션 타입 삽입
        		new Promotion(1,"COOPON", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1),null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 200, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );
    	
    	//when
        ProductRelatedException exception = assertThrows(ProductRelatedException.class, () -> {
            promotionService.isInvailedPomotion(promotionList);
        });
        
        //then
        assertEquals(ErrorCode.INVALID_PROMOTION_TYPE, exception.getErrorCode());
    }
    


}






