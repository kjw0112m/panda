package com.kh17.panda.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrdersDto {
	private String member_id,order_dt, pay_status,cs_status, t_status, pay_type, order_id, t_invoice, t_id, sizes, team, re_name, re_phone, re_addr;
	private int id,product_id,quantity,total_price,discount_price, orders;

}
