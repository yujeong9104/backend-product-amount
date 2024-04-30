package antigravity.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;

@SpringBootTest
public class PromotionProductsRepositoryTest {
	
	@Autowired
	private PromotionProductsRepository promotionProductsRepository;
	
	@Test
	@Transactional
	public void test(){
		
		System.out.println(promotionProductsRepository.findPromotionIdByProductId(1));
		
    }

 
}
