package antigravity.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.PromotionProductsRepository;
import exception.ErrorCode;
import exception.ProductRelatedException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromotionProductsService {
	
	private final PromotionProductsRepository promotionProductsRepository;
	
	@Transactional(readOnly = true)
	//프로모션이 해당 상품에 사용 가능한 것인가 검사
	public void isPromotionProductsAbled(int productId, List<Promotion> promotionList) {
		List<Integer> promotionIdList = promotionList.stream().map(p->p.getId()).collect(Collectors.toList());
		Set<Integer> promotionIdset = new HashSet<>(promotionIdList);
		
		List<PromotionProducts> promotionProducts = promotionProductsRepository.findByProductId(productId);
		List<Integer> promotionIdListFromPromotionProducts = promotionProducts
				.stream().map(p->p.getId()).distinct().collect(Collectors.toList());
		Set<Integer> promotionIdSetFromPromotionProducts = new HashSet<>(promotionIdListFromPromotionProducts);
		
		if(!promotionIdSetFromPromotionProducts.containsAll(promotionIdset)) {
			throw new ProductRelatedException(ErrorCode.UNAPPLICABLE_PROMOTION);
		}
	}
	
	public ProductAmountResponse applyPromotionOnProduct(Product product, List<Promotion> promotionList) {
		
		int originPrice = product.getPrice();
		int discountPrice = 0;
		int finalPrice = product.getPrice();
		
		for(Promotion p : promotionList) {
			//10000보다 같거나 작으면 그만둠
			
			String promotionType = p.getPromotionType();
			switch (promotionType) {
			case "COUPON": //금액할인
				//discountType이 "WON"이어야 함
				//PROMOTION_DISCOUNT_TYPE_MISMATCH
				
				break;
			case "CODE": //%할인
				//discountType이 "PERCENT"이어야 함
				//PROMOTION_DISCOUNT_TYPE_MISMATCH
				
				break;
			default:  //유효하지 않은 프로모션 타입입니다.
				//INVALID_PROMOTION_TYPE
				break;
			}
			
		}
		
		//최소 상품가격은 ₩ 10,000 입니다.
		//최대 상품가격은 ₩ 10,000,000 입니다.
		//최종 상품 금액은 천단위 절삭합니다.
		
		return new ProductAmountResponse(product.getName(),originPrice,discountPrice,finalPrice);
	}
	



}
