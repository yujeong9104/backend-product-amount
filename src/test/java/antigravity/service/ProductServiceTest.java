package antigravity.service;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import antigravity.domain.entity.Product;
import antigravity.enums.ErrorCode;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionRepository;
import exception.ProductRelatedException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	
	@InjectMocks
	private ProductService productService;
	
	@Mock
	private ProductRepository productRepostory;
	@Mock
	private PromotionRepository promotionRepostory;

    
    @Test
    @Transactional
    @DisplayName("상품 조회 불가능 오류")
    void productNotFountTest() {
    	
    	given(productRepostory.findById(any())).willReturn(Optional.empty());
    	
    	ProductRelatedException e = assertThrows(ProductRelatedException.class, () -> {
    		productService.isProductExists(1);
    	});
    	
    	assertEquals(ErrorCode.PRODUCT_NOT_FOUND,e.getErrorCode());
    	
    }
    
}






