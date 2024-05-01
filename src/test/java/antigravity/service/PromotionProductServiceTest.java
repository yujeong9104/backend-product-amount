package antigravity.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import antigravity.controller.ProductRelatedException;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.enums.ErrorCode;
import antigravity.enums.Price;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.PromotionProductsRepositoryEM;

@ExtendWith(MockitoExtension.class)
public class PromotionProductServiceTest {
	
	@InjectMocks
	private PromotionProductsService promotionProductsService;
	
	@Mock
	private PromotionProductsRepositoryEM promotionProductsRepository;

    @Test
    @DisplayName("프로모션이 해당 상품에 사용 가능한 것인가 검사 - 성공")
    void isPromotionProductsAbledTestCompleted() {
    	
    	//given
        int productId = 1;
        List<Promotion> promotionList = new ArrayList<>();
        Promotion promotion = new Promotion();
        promotion.setId(productId);
        promotionList.add(promotion);
        
        //when
        when(promotionProductsRepository.findPromotionIdByProductId(productId)).thenReturn(Arrays.asList(1,2,3));
        
        //then
        assertDoesNotThrow(() -> promotionProductsService.isPromotionProductsAbled(productId, promotionList));
 
    }
	
    
    @Test
    @DisplayName("프로모션이 해당 상품에 사용 가능한 것인가 검사 - 오류")
    void isPromotionProductsAbledTest() {
    	
    	//given
    	int productId = 4;
        List<Promotion> promotionList = new ArrayList<>();
        Promotion promotion = new Promotion();
        promotion.setId(productId);
        promotionList.add(promotion);
        
        //when
        when(promotionProductsRepository.findPromotionIdByProductId(productId)).thenReturn(Arrays.asList(1,2,3));
        
    	ProductRelatedException e = assertThrows(ProductRelatedException.class, () -> {
    		promotionProductsService.isPromotionProductsAbled(productId, promotionList);
    	});
    	
    	//then
    	assertEquals(ErrorCode.UNAPPLICABLE_PROMOTION,e.getErrorCode());
        
    }

    @Test
    @DisplayName("프로모션 적용 가격")
    public void ApplyPromotionOnProductTest() {
    	
    	//given
    	String productName = "상품";
    	Product product = new Product();
    	product.setName(productName);
    	product.setPrice(22200);
    	
        List<Promotion> promotionList = List.of(
        		new Promotion(1,"COUPON", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1), null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1), null)
        );
        
        // When
        ProductAmountResponse response = promotionProductsService.applyPromotionOnProduct(product, promotionList);
        
        // Then
        assertEquals(productName, response.getName());
        assertEquals(22200, response.getOriginPrice());
        assertEquals(6200, response.getDiscountPrice());
        assertEquals(16000, response.getFinalPrice());

    }
    
    @Test
    @DisplayName("프로모션 적용 가격은 최소 FINAL_MIN_PRICE")
    public void ApplyPromotionOnProductTestFinalMinPrice() {
    	
    	//given
    	Product product = new Product();
    	product.setName("상품1");
    	product.setPrice(1200);
    	
        List<Promotion> promotionList = List.of(
        		new Promotion(1,"COUPON", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1), null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1), null)
        );
        
        // When
        ProductAmountResponse response = promotionProductsService.applyPromotionOnProduct(product, promotionList);
        
        // Then
        assertEquals("상품1", response.getName());
        assertEquals(1200, response.getOriginPrice());
        assertEquals(1200, response.getDiscountPrice());
        assertEquals(Price.FINAL_MIN_PRICE.getPrice(), response.getFinalPrice());

    }
    
    /*
	최소 상품가격, 최대 상품가격이 쿠폰 적용 후 가격인 경우
    
    @Test
    @DisplayName("프로모션 최소 적용 가격")
    public void testApplyPromotionOnProduct() {
    	
    	//given
    	Product product = new Product();
    	product.setName("상품1");
    	product.setPrice(12000);
        List<Promotion> promotionList = List.of(
        		new Promotion(1,"COUPON", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1), null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1), null)
        );
        
        // When
        ProductAmountResponse response = promotionProductsService.applyPromotionOnProduct(product, promotionList);
        
        // Then
        assertEquals("상품1", response.getName());
        assertEquals(12000, response.getOriginPrice());
        assertEquals(2000, response.getDiscountPrice());
        assertEquals(10000, response.getFinalPrice());

    }
    
    
    @Test
    @DisplayName("프로모션 최대 적용 가격")
    public void testApplyPromotionOnProductTestMaximum() {
    
    	//given
    	Product product = new Product();
    	product.setName("상품1");
    	product.setPrice(19000000);
        List<Promotion> promotionList = List.of(
        		new Promotion(1,"COUPON", "프로모션1", "WON", 1000, LocalDate.now(), LocalDate.now().plusDays(1), null),
                new Promotion(2,"CODE", "프로모션2", "PERCENT", 20, LocalDate.now(), LocalDate.now().plusDays(1), null)
        );
        
        // When
        ProductAmountResponse response = promotionProductsService.applyPromotionOnProduct(product, promotionList);
        
        // Then
        assertEquals("상품1", response.getName());
        assertEquals(19000000, response.getOriginPrice());
        assertEquals(9000000, response.getDiscountPrice());
        assertEquals(10000000, response.getFinalPrice());

    }
    
    */
	
}
