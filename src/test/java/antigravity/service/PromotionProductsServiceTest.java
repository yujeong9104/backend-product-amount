package antigravity.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.enums.ErrorCode;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import antigravity.repository.PromotionRepository;
import exception.ProductRelatedException;

@ExtendWith(MockitoExtension.class)
public class PromotionProductsServiceTest {
	
	@InjectMocks
	private PromotionProductsService promotionProductsService;
	
	@Mock
	private PromotionProductsRepository promotionProductsRepository;

    @Test
    @DisplayName("프로모션이 해당 상품에 사용 가능한 것인가 검사 - 완료")
    void isPromotionProductsAbledTestCompleted() {
        int productId = 1;
        List<Promotion> promotionList = new ArrayList<>();
        Promotion promotion = new Promotion();
        promotion.setId(productId);
        promotionList.add(promotion);
        
        when(promotionProductsRepository.findPromotionIdByProductId(productId)).thenReturn(Arrays.asList(1,2,3));

        assertDoesNotThrow(() -> promotionProductsService.isPromotionProductsAbled(productId, promotionList));
 
    }
	
    
    @Test
    @DisplayName("프로모션이 해당 상품에 사용 가능한 것인가 검사 - 오류")
    void isPromotionProductsAbledTest() {
    	int productId = 4;
        
        List<Promotion> promotionList = new ArrayList<>();
        Promotion promotion = new Promotion();
        promotion.setId(productId);
        promotionList.add(promotion);
        
        when(promotionProductsRepository.findPromotionIdByProductId(productId)).thenReturn(Arrays.asList(1,2,3));

    	ProductRelatedException e = assertThrows(ProductRelatedException.class, () -> {
    		promotionProductsService.isPromotionProductsAbled(productId, promotionList);
    	});
    	
    	assertEquals(ErrorCode.UNAPPLICABLE_PROMOTION,e.getErrorCode());
        
    }
    
	
}
