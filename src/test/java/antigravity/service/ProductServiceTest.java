package antigravity.service;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import antigravity.controller.ProductRelatedException;
import antigravity.domain.entity.Product;
import antigravity.enums.ErrorCode;
import antigravity.enums.Price;
import antigravity.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	
	@InjectMocks
	private ProductService productService;
       
	@Mock
	private ProductRepository productRepository;

    @Test
    @DisplayName("상품 조회 성공")
    void productNotFountTestCompleted() {
    	
    	//given
    	int productId = anyInt();
    	Product product = new Product();
    	given(productRepository.findById(productId)).willReturn(Optional.of(product));
    	
    	//when, then
    	assertDoesNotThrow(() -> productService.isProductExists(productId));
    	
    }

    
    @Test
    @DisplayName("상품 조회 오류")
    void productNotFountTest() {
    	
    	//given
    	int productId = anyInt();
    	given(productRepository.findById(productId)).willReturn(Optional.empty());
    	
    	//when
    	ProductRelatedException e = assertThrows(ProductRelatedException.class, () -> {
    		productService.isProductExists(productId);
    	});
    	
    	//then
    	assertEquals(ErrorCode.PRODUCT_NOT_FOUND,e.getErrorCode());
    	
    }
    
    @Test
    @DisplayName("적정 상품 가격 성공")
    void isProductPriceValidTestCompleted(){
    	
    	//given
    	int price = Price.MIN_PRICE.getPrice() + 1;

    	//when, then
    	assertDoesNotThrow(() -> productService.isProductPriceValid(price));
	
    }
    
    @Test
    @DisplayName("적정 상품 가격 미만 오류")
    void isProductPriceValidTestUnder(){
    	
    	//given
    	int price = Price.MIN_PRICE.getPrice() - 1;
    	
    	//when
    	ProductRelatedException e = assertThrows(ProductRelatedException.class, () -> {
    		productService.isProductPriceValid(price);
    	});
    	
    	//then
    	assertEquals(ErrorCode.PRODUCT_UNDER_MIN_PRICE,e.getErrorCode());
    	
    }
    
    @Test
    @DisplayName("적정 상품 가격 초과 오류")
    void isProductPriceValidTestUpper(){
    	
    	//given
    	int price = Price.MAX_PRICE.getPrice() + 1;
    	
    	//when
    	ProductRelatedException e = assertThrows(ProductRelatedException.class, () -> {
    		productService.isProductPriceValid(price);
    	});
    	
    	//then    	
    	assertEquals(ErrorCode.PRODUCT_UPPER_MAX_PRICE,e.getErrorCode());
    	
    }
    
}






