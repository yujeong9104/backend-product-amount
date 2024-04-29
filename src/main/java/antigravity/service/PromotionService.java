package antigravity.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antigravity.domain.entity.Promotion;
import antigravity.enums.ErrorCode;
import antigravity.repository.PromotionRepository;
import exception.ProductRelatedException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromotionService {
	
	private final PromotionRepository promotionRepository;
	
	@Transactional(readOnly = true)
	public List<Promotion> isPromotionsAbled(int[] couponIds) {
		
		//중복된 프로모션이 존재하는가 검사
		int couponIdsLength = couponIds.length;
		IntStream couponIdStream = IntStream.of(couponIds);
		if(couponIdStream.distinct().count() != couponIdsLength) {
			throw new ProductRelatedException(ErrorCode.DUPLICATE_PROMOTION);
		}
		
		//존재하지 않는 프로모션이 하나라도 있는가 검사
		List<Integer> CouponIdsList = couponIdStream
                .boxed()
                .collect(Collectors.toList());
		List<Promotion> promotionList = promotionRepository.findByIdIn(CouponIdsList);
		if(promotionList.size() !=couponIdsLength) {
			throw new ProductRelatedException(ErrorCode.PROMOTION_NOT_FOUND);
		}
		
		//프모로션의 유효기간이 맞는가 검사
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		LocalDate currentDateInKorea = LocalDate.now(zoneId);
		for(Promotion promotion : promotionList) {
			if(promotion.getUseStartedAt().isAfter(currentDateInKorea) || promotion.getUseEndedAt().isBefore(currentDateInKorea) ) {
				throw new ProductRelatedException(ErrorCode.INVALID_PROMOTION_PERIOD);
			}
		}
		
		return promotionList;
	}
	

}
