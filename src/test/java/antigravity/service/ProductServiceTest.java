package antigravity.service;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
	private ProductRepository productRepostory;

    @Test
    @DisplayName("상품 조회 성공")
    void productNotFountTestCompleted() {
    	int productId = 2;
    	
        Product fakeProduct = new Product();
        fakeProduct.setId(productId);
        fakeProduct.setName("상품1");
        fakeProduct.setPrice(1000);
    	
    	given(productRepostory.findById(productId)).willReturn(Optional.of(fakeProduct));
    	
    	assertDoesNotThrow(() -> productService.isProductExists(productId));
    	
    }
	
	
    
    @Test
    @DisplayName("상품 조회 오류")
    void productNotFountTest() {
    	int productId = 3;
    	
    	given(productRepostory.findById(productId)).willReturn(Optional.empty());
    	
    	ProductRelatedException e = assertThrows(ProductRelatedException.class, () -> {
    		productService.isProductExists(productId);
    	});
    	
    	assertEquals(ErrorCode.PRODUCT_NOT_FOUND,e.getErrorCode());
    	
    }
    
    @Test
    @DisplayName("적정 상품 가격")
    void isProductPriceValidTestCompleted(){

    	assertDoesNotThrow(() -> productService.isProductPriceValid(Price.MIN_PRICE.getPrice() + 1));
	
    }
    
    @Test
    @DisplayName("적정 상품 가격 미만")
    void isProductPriceValidTestUnder(){

    	ProductRelatedException e = assertThrows(ProductRelatedException.class, () -> {
    		productService.isProductPriceValid(Price.MIN_PRICE.getPrice() - 1);
    	});
    	
    	assertEquals(ErrorCode.PRODUCT_UNDER_MIN_PRICE,e.getErrorCode());
    	
    }
    
    @Test
    @DisplayName("적정 상품 가격 초과")
    void isProductPriceValidTestUpper(){

    	ProductRelatedException e = assertThrows(ProductRelatedException.class, () -> {
    		productService.isProductPriceValid(Price.MAX_PRICE.getPrice() + 1);
    	});
    	
    	assertEquals(ErrorCode.PRODUCT_UPPER_MAX_PRICE,e.getErrorCode());
    	
    }
    
}






