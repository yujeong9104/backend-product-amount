package antigravity.domain.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@Column(name = "name", nullable = false)
    private String name;
	
	@Column(name = "price", nullable = false)
    private int price;
	
	@OneToMany(mappedBy="product", cascade=CascadeType.REMOVE)
	private List<PromotionProducts> promotionProductsList;
	
}
