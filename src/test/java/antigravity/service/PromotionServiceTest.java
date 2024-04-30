package antigravity.service;


import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;

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
    	 int[] couponIds = {1, 2, 3};
    	
    	 assertDoesNotThrow(() -> promotionService.isDuplicatedPromotion(couponIds));

    }

    @Test
    @DisplayName("중복된 프로모션이 존재하는가 검사 - 오류")
    void productNotFoundTest() {
    	 int[] couponIds = {1, 2, 3, 3};
    	
    	ProductRelatedException e = assertThrows(ProductRelatedException.class, () -> {
    		promotionService.isDuplicatedPromotion(couponIds);
    	});
    	
    	assertEquals(ErrorCode.DUPLICATE_PROMOTION,e.getErrorCode());
    }
    
    @Test
    @DisplayName("존재하지 않는 프로모션이 하나라도 있는가 검사 - 성공")
    void isPromotionNotFoundTestCompleted() {
        int[] couponIds = {1, 2}; 

        List<Promotion> promotionList = List.of(
                new Promotion(1,"쿠폰", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1),null),
                new Promotion(2,"코드", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );

        assertDoesNotThrow(() -> promotionService.isPromotionNotFound(couponIds, promotionList));

    }
        
    
    @Test
    @DisplayName("존재하지 않는 프로모션이 하나라도 있는가 검사 - 오류")
    void isPromotionNotFoundTest() {
        int[] couponIds = {1, 2, 3}; // 존재하지 않는 프로모션 ID가 있는 경우

        List<Promotion> promotionList = List.of(
                new Promotion(1,"쿠폰", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1),null),
                new Promotion(2,"코드", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );

        ProductRelatedException exception = assertThrows(ProductRelatedException.class, () -> {
            promotionService.isPromotionNotFound(couponIds, promotionList);
        });

        assertEquals(ErrorCode.PROMOTION_NOT_FOUND, exception.getErrorCode());
    }
    
    @Test
    @DisplayName("프모로션 유효성 검사 - 성공")
    void testIsInvalidPromotionTestCompleted() {
        List<Promotion> promotionList = List.of(
        		// 유효하지 않는 프로모션 기간 삽입
        		new Promotion(1,"COUPON", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1), null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1), null)
        );
        
        assertDoesNotThrow(() -> promotionService.isInvailedPomotion(promotionList));

    }
    
    @Test
    @DisplayName("프모로션의 유효기간이 맞는가 검사 - 오류")
    void testIsInvalidPromotionTestPeriod() {
        List<Promotion> promotionList = List.of(
        		// 유효하지 않는 프로모션 기간 삽입
        		new Promotion(1,"COUPON", "프로모션1", "WON", 1000, LocalDate.now().plusDays(1), LocalDate.now(),null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );

        ProductRelatedException exception = assertThrows(ProductRelatedException.class, () -> {
            promotionService.isInvailedPomotion(promotionList);
        });

        assertEquals(ErrorCode.INVALID_PROMOTION_PERIOD, exception.getErrorCode());
    }
    
    @Test
    @DisplayName("프모로션 타입과 할인타입이 일치하는가 검사 - 오류")
    void testIsInvalidPromotiontTestTypeMatch() {
        List<Promotion> promotionList = List.of(
        		// 유효하지 않는 프로모션 기간 삽입
        		new Promotion(1,"COUPON", "프로모션1", "PERCENT", 10, LocalDate.now(), LocalDate.now().plusDays(1),null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );

        ProductRelatedException exception = assertThrows(ProductRelatedException.class, () -> {
            promotionService.isInvailedPomotion(promotionList);
        });
        assertEquals(ErrorCode.PROMOTION_DISCOUNT_TYPE_MISMATCH, exception.getErrorCode());
    }
    
    @Test
    @DisplayName("프모로션 PERCENT 값이 유효한가 검사 - 오류")
    void testIsInvalidPromotiontTestPercent() {
        List<Promotion> promotionList = List.of(
        		// 유효하지 않는 프로모션 기간 삽입
        		new Promotion(1,"COUPON", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1),null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 200, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );

        ProductRelatedException exception = assertThrows(ProductRelatedException.class, () -> {
            promotionService.isInvailedPomotion(promotionList);
        });
        assertEquals(ErrorCode.INVALID_PROMOTION_VALUE, exception.getErrorCode());
    }
    
    @Test
    @DisplayName("프모로션 타입이 유효한가 검사 - 오류")
    void testIsInvalidPromotiontTestType() {
        List<Promotion> promotionList = List.of(
        		// 유효하지 않는 프로모션 기간 삽입
        		new Promotion(1,"COOPON", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1),null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 200, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );

        ProductRelatedException exception = assertThrows(ProductRelatedException.class, () -> {
            promotionService.isInvailedPomotion(promotionList);
        });
        assertEquals(ErrorCode.INVALID_PROMOTION_TYPE, exception.getErrorCode());
    }
    
    
    @Test
    @DisplayName("프로모션 아이디들로 프로모션 리스트 리턴")
    void testGetPromotionList() {
        int[] couponIds = {1, 2}; // 테스트에 사용할 프로모션 ID 배열

        List<Promotion> promotionList = List.of(
        		new Promotion(1,"쿠폰", "프로모션1", "WON", 1000, LocalDate.now().plusDays(1), LocalDate.now(),null),
                new Promotion(2,"코드", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1),null)
        );
        
        when(promotionRepository.findByIdIn(anyList())).thenReturn(promotionList);

        List<Promotion> result = promotionService.getPromotionList(couponIds);

        assertEquals(promotionList.size(), result.size());
        assertTrue(promotionList.containsAll(result));
    }


}






