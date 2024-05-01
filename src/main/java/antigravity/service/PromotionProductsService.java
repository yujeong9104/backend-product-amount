package antigravity.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antigravity.controller.ProductRelatedException;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.enums.ErrorCode;
import antigravity.enums.Price;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.PromotionProductsRepositoryEM;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromotionProductsService {
	
	@Autowired
	private final PromotionProductsRepositoryEM promotionProductsRepositoryEM;
	
	@Transactional(readOnly = true)
	//프로모션이 해당 상품에 사용 가능한 것인가 검사
	public void isPromotionProductsAbled(int productId, List<Promotion> promotionList) {
		List<Integer> promotionIdList = promotionList.stream().map(p->p.getId()).collect(Collectors.toList());
		Set<Integer> promotionIdset = new HashSet<>(promotionIdList);
		
		List<Integer> promotionIdListFromPromotionProducts = promotionProductsRepositoryEM.findPromotionIdByProductId(productId);
		Set<Integer> promotionIdSetFromPromotionProducts = new HashSet<>(promotionIdListFromPromotionProducts);
		
		if(!promotionIdSetFromPromotionProducts.containsAll(promotionIdset)) {
			throw new ProductRelatedException(ErrorCode.UNAPPLICABLE_PROMOTION);
		}
	}

	public ProductAmountResponse applyPromotionOnProduct(Product product, List<Promotion> promotionList) {
		
		int originPrice = product.getPrice();
		double double_finalPrice = product.getPrice();
		
		boolean isFinalMinPrice = false;
		for(Promotion p : promotionList) {
			/*
			최소 상품가격이 쿠폰 적용 후 가격이라면
			if(double_finalPrice<=Price.MIN_PRICE.getPrice()) {
				break;
			}
			*/
			if(double_finalPrice<=Price.FINAL_MIN_PRICE.getPrice()) {
				isFinalMinPrice = true;
				break;
			}
			String promotionType = p.getPromotionType();
			double gap = 0;
			switch (promotionType) {
				case "COUPON": //금액할인
					gap = p.getDiscountValue();
					double_finalPrice -= gap;
					break;
				case "CODE": //%할인
					double persent = p.getDiscountValue()*(0.01);
					gap = originPrice*persent;
					double_finalPrice -= gap;
					break;
				default:
					throw new ProductRelatedException(ErrorCode.INVALID_PROMOTION_TYPE);
			}	
		}
		
		/*
		최소 상품가격, 최대 상품가격이 쿠폰 적용 후 가격인 경우
		if(double_finalPrice<Price.MIN_PRICE.getPrice()) {
			double_finalPrice = Price.MIN_PRICE.getPrice();
		}else if(double_finalPrice> Price.MAX_PRICE.getPrice()) {
			double_finalPrice = Price.MAX_PRICE.getPrice();
		}
		*/
		
		int finalPrice = Price.FINAL_MIN_PRICE.getPrice();
		if(!isFinalMinPrice) {
			finalPrice = (int) (double_finalPrice/Price.UNIT_PRICE.getPrice())*Price.UNIT_PRICE.getPrice();
		}
		
		int discountPrice = originPrice - finalPrice;
		
		return new ProductAmountResponse(product.getName(),originPrice,discountPrice,finalPrice);
	}
	


}
