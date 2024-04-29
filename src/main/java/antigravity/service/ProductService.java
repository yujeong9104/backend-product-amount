package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import exception.ErrorCode;
import exception.ProductRelatedException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
    	
    	Product product = isProductExists(request.getProductId());
        
    	
        return null;
        
    }
    
    @Transactional(readOnly = true)
    public Product isProductExists (int productId) {
    	Optional <Product> opProduct = productRepository.findById(productId);
    	opProduct.orElseThrow(() -> new ProductRelatedException(ErrorCode.PRODUCT_NOT_FOUND));
        return opProduct.get();
    }
    
}
