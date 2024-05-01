package antigravity.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@Column(name = "promotion_type", nullable = false)
    private String promotionType; //쿠폰 타입 (쿠폰, 코드)
	
	@Column(name = "name", nullable = false)
    private String name;
	
	@Column(name = "discount_type", nullable = false)
    private String discountType; // WON : 금액 할인, PERCENT : %할인
	
	@Column(name = "discount_value", nullable = false)
    private int discountValue; // 할인 금액 or 할인 %
	
	@Column(name = "use_started_at", nullable = false)
    private LocalDate useStartedAt; // 쿠폰 사용가능 시작 기간
	
	@Column(name = "use_ended_at", nullable = false)
    private LocalDate useEndedAt; // 쿠폰 사용가능 종료 기간
	
	@OneToMany(mappedBy="promotion", cascade=CascadeType.REMOVE)
	private List<PromotionProducts> promotionProductsList;
}
