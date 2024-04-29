package antigravity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import antigravity.domain.entity.PromotionProducts;

public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Integer>{
	List<PromotionProducts> findByProductId(int productId);
}
