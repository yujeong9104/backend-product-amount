package antigravity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import antigravity.domain.entity.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion,Integer>{

}
