package antigravity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Price {

	MAX_PRICE(10000000),
	MIN_PRICE(10000),
	FINAL_MIN_PRICE(0),
	UNIT_PRICE(1000);
	
	private final int price;
	
}
