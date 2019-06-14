package com.zhongjian.dto.cart.route;



import lombok.Data;

@Data
public class BasketCVSidDTO {
	
	 private static final long serialVersionUID = 197018972999527001L;

	private int[] cvSids;
	
	private int[] basketSids;

}
