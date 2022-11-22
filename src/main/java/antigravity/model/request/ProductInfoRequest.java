package antigravity.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInfoRequest {
    private int productId;
    private int[] couponIds;
}
