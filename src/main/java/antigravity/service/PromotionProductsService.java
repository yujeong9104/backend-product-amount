package antigravity.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.enums.ErrorCode;
import antigravity.enums.Price;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.PromotionProductsRepository;
import exception.ProductRelatedException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromotionProductsService {
	@Autowired
	private final PromotionProductsRepository promotionProductsRepository;
	
	@Transactional(readOnly = true)
	//프로모션이 해당 상품에 사용 가능한 것인가 검사
	public void isPromotionProductsAbled(int productId, List<Promotion> promotionList) {
		List<Integer> promotionIdList = promotionList.stream().map(p->p.getId()).collect(Collectors.toList());
		Set<Integer> promotionIdset = new HashSet<>(promotionIdList);
		
		List<Integer> promotionIdListFromPromotionProducts = promotionProductsRepository.findPromotionIdByProductId(productId);
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
			if(finalPrice<=Price.MIN_PRICE.getPrice()) {
				break;
			}
			String promotionType = p.getPromotionType();
			int gap = 0;
			switch (promotionType) {
			case "COUPON": //금액할인
				if(!p.getPromotionType().equals("WON")) {
					throw new ProductRelatedException(ErrorCode.PROMOTION_DISCOUNT_TYPE_MISMATCH);
				}
				gap = p.getDiscountValue();
				discountPrice += gap;
				finalPrice -= gap;
				break;
			case "CODE": //%할인
				if(!p.getPromotionType().equals("PERCENT")) {
					throw new ProductRelatedException(ErrorCode.PROMOTION_DISCOUNT_TYPE_MISMATCH);
				}
				int persent = p.getDiscountValue();
				if(persent < 0 || persent > 100) {
					throw new ProductRelatedException(ErrorCode.INVALID_PROMOTION_VALUE);
				}
				gap = originPrice*persent;
				discountPrice += gap;
				finalPrice -= gap;
				break;
			default:
				throw new ProductRelatedException(ErrorCode.INVALID_PROMOTION_TYPE);
			}
			
		}
		
		if(finalPrice<Price.MIN_PRICE.getPrice()) {
			discountPrice -= Price.MIN_PRICE.getPrice()-finalPrice;
			finalPrice = Price.MIN_PRICE.getPrice();
		}else if(finalPrice> Price.MAX_PRICE.getPrice()) {
			discountPrice += Price.MAX_PRICE.getPrice()-finalPrice;
			finalPrice = Price.MAX_PRICE.getPrice();
		}
		
		discountPrice += finalPrice%Price.UNIT_PRICE.getPrice();
		finalPrice = (finalPrice/Price.UNIT_PRICE.getPrice())*Price.UNIT_PRICE.getPrice();
		
		return new ProductAmountResponse(product.getName(),originPrice,discountPrice,finalPrice);
	}
	



}
