package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.enums.ErrorCode;
import antigravity.enums.Price;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import exception.ProductRelatedException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    
    private final PromotionService promotionService;
    
    private final PromotionProductsService promotionProductsService;
    
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
    	//유효성 검사
    	Product product = isProductExists(request.getProductId());
    	List <Promotion> promotionList = promotionService.isPromotionsAbled(request.getCouponIds());
    	promotionProductsService.isPromotionProductsAbled(product.getId(),promotionList);
    	//할인 계산
    	ProductAmountResponse prouductAmountResponse = promotionProductsService.applyPromotionOnProduct(product,promotionList);
    	return prouductAmountResponse;
        
    }
    
	@Transactional(readOnly = true)
    public Product isProductExists (int productId) {
		//존재하는 상품인가 검사
    	Optional <Product> optionalProduct = productRepository.findById(productId);
    	optionalProduct.orElseThrow(() -> new ProductRelatedException(ErrorCode.PRODUCT_NOT_FOUND));
    	return optionalProduct.get();
        
    }
    
}
