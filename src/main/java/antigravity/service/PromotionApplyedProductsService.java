package antigravity.service;

import java.util.List;

import org.springframework.stereotype.Service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromotionApplyedProductsService {
	private final ProductService productService;
    private final PromotionService promotionService;
    private final PromotionProductsService promotionProductsService;
    
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
    	int productId = request.getProductId();
    	int[] couponIds = request.getCouponIds();
    	//유효성 검사 및 프로덕트, 프로모션 정보 조회
    	Product product = productService.isProductExists(productId);
    	productService.isProductPriceValid(product.getPrice());
    	promotionService.isDuplicatedPromotion(couponIds);
    	List<Promotion> promotionList = promotionService.getPromotionList(couponIds);
    	promotionService.isPromotionNotFound(couponIds, promotionList);
    	promotionService.isInvailedPomotion(promotionList);
    	promotionProductsService.isPromotionProductsAbled(productId,promotionList);
    	//할인 계산
    	ProductAmountResponse prouductAmountResponse = promotionProductsService.applyPromotionOnProduct(product,promotionList);
    	return prouductAmountResponse;
        
    }
}
