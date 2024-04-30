package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.enums.ErrorCode;
import antigravity.enums.Price;
import antigravity.repository.ProductRepository;
import exception.ProductRelatedException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    
    //존재하는 상품인가 검사
	@Transactional(readOnly = true)
    public Product isProductExists (int productId) {
    	Optional <Product> optionalProduct = productRepository.findById(productId);
    	optionalProduct.orElseThrow(() -> new ProductRelatedException(ErrorCode.PRODUCT_NOT_FOUND));
    	return optionalProduct.get();
    }
	
	//상품 최소, 최대 가격 검사
	public void isProductPriceValid (int productPrice) {
		if(productPrice<Price.MIN_PRICE.getPrice()) {
			throw new ProductRelatedException(ErrorCode.PRODUCT_UNDER_MIN_PRICE);
		}
		if(productPrice>Price.MAX_PRICE.getPrice()) {
			throw new ProductRelatedException(ErrorCode.PRODUCT_UPPER_MAX_PRICE);
		}
	}
    
}
