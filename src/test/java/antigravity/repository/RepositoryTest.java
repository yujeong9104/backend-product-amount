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
public class RepositoryTest {
	
	@Autowired
	private PromotionProductsRepository promotionProductsRepository;
	
	@Autowired
	private PromotionRepository promotionRepository;
	
	@Test
	@Transactional
	public void test(){
		
		List<PromotionProducts> promotionProducts = promotionProductsRepository.findByProductId(1);
		for(PromotionProducts pp:promotionProducts) {
			//System.out.println(pp.getId());
		}
		
		List<Integer> temp = new ArrayList<>();
		temp.add(1);
		temp.add(2);
		
		List<Promotion> promotionList = promotionRepository.findByIdIn(temp);
		List<Integer> promotionIdList = promotionList.stream().map(p->p.getId()).collect(Collectors.toList());
		//promotionIdList.stream().forEach(System.out::println);
		
		
		List<Integer> list = Arrays.asList(1, 2, 3, 3, 4, 5, 5);
		List<Integer> uniqueList = list.stream()
		                               .distinct()         
		                               .collect(Collectors.toList());
		
		uniqueList.stream().forEach(System.out::println);
		

		
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(5, 4, 3, 2, 1);

        boolean result = areListsEqual(list1, list2);
        System.out.println("Lists are equal: " + result);
    }

    public static boolean areListsEqual(List<Integer> list1, List<Integer> list2) {
        // 리스트를 집합으로 변환합니다.
        Set<Integer> set1 = new HashSet<>(list1);
        Set<Integer> set2 = new HashSet<>(list2);

        // 집합들이 같은지를 확인합니다.
        return set1.equals(set2);
    }

}
