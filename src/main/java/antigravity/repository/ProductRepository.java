package antigravity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import antigravity.domain.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Integer>{

}