package antigravity.service;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import antigravity.domain.entity.Product;
import antigravity.enums.ErrorCode;
import antigravity.repository.ProductRepository;
import exception.ProductRelatedException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	
	@InjectMocks
	private ProductService productService;
	
	@Mock
	private ProductRepository productRepostory;

    @Transactional
    @Test
    void isProductExistsTest() {
    	
    	given(productRepostory.findById(any())).willReturn(Optional.empty());
    	
    	ProductRelatedException e= assertThrows(ProductRelatedException.class, () -> {
    		productService.isProductExists(1);
    	});
    	
    	assertEquals(ErrorCode.PRODUCT_NOT_FOUND,e.getErrorCode());
    	
    }
}