package antigravity.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import antigravity.domain.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Integer>{

}