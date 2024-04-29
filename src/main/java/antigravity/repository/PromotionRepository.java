package antigravity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import antigravity.domain.entity.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion,Integer>{
	List<Promotion> findByIdIn(List<Integer> ids);
}
