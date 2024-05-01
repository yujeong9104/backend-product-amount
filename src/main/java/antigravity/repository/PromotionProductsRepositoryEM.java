package antigravity.repository;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class PromotionProductsRepositoryEM{
	
	@PersistenceContext
    private EntityManager em;
	
    public List<Integer> findPromotionIdByProductId(int productId) {
        return em.createQuery("SELECT p.promotion.id FROM PromotionProducts p WHERE p.product.id = :productId", Integer.class)
                .setHint("org.hibernate.readOnly", true)
                .setParameter("productId", productId)
                .getResultList();
    }
	
	
}
