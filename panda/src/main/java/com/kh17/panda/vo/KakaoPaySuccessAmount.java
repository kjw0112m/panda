package com.kh17.panda.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//카카오페이 결제 성공시 수량 정보를 저장하기 위한 클래스
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class KakaoPaySuccessAmount {
	private int total, tax_free, vat, point;
}
