<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>
<script src="https://code.jquery.com/jquery-latest.js"></script>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script>
	$(function() {
		function uncomma(str) {
			str = String(str);
			return str.replace(/[^\d]+/g, '');
		}

		$(".btn-addr").click(findAddress);

		$('#same_info').change(
				function() {
					if ($(this).is(':checked')) {
						$('.phone').eq(3).val($('.phone').eq(0).val());
						$('.phone').eq(4).val($('.phone').eq(1).val());
						$('.phone').eq(5).val($('.phone').eq(2).val());

						$('.name').eq(1).val($('.name').eq(0).val());

						$("input[id=post_code]").eq(1).val(
								$("input[id=post_code]").eq(0).val());
						$("input[id=basic_addr]").eq(1).val(
								$("input[id=basic_addr]").eq(0).val());
						$("input[id=detail_addr]").eq(1).val(
								$("input[id=detail_addr]").eq(0).val());
					}
					;
				});

		$('#new_info').change(function() {
			if ($(this).is(':checked')) {
				$('#new_body').find('input[type=text]').val('');
			}
		});

		$("form")
				.submit(
						function(e) {
							e.preventDefault();
							if ($(this).attr('standby')) {
								var total_amount = $('input[name=total_amount]')
										.val();
								$('input[name=total_amount]').val(
										uncomma(total_amount));
								if ($('input[name=pay_type]:checked').val() == '카카오페이') {
									$(this)
											.attr('action',
													'${pageContext.request.contextPath}/pay/kakao/confirm');

									window
											.open("", "kakaopay",
													"width=730,height=520,left=50, top=50");
									this.target = 'kakaopay';
								}
							}
							this.submit();
						});

	});
	function findAddress() {
		var thiz = $(this);
		new daum.Postcode({
			oncomplete : function(data) {
				var addr = ''; // 주소 변수
				var extraAddr = ''; // 참고항목 변수

				if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
					addr = data.roadAddress;
				} else {
					addr = data.jibunAddress;
				}

				if (data.userSelectedType === 'R') {
					if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
						extraAddr += data.bname;
					}
					if (data.buildingName !== '' && data.apartment === 'Y') {
						extraAddr += (extraAddr !== '' ? ', '
								+ data.buildingName : data.buildingName);
					}
					if (extraAddr !== '') {
						extraAddr = ' (' + extraAddr + ')';
					}
				}
				thiz.parent().find("input[id=post_code]").val(data.zonecode);
				thiz.parent().find("input[id=basic_addr]").val(addr);
				thiz.parent().find("input[id=detail_addr]").focus();
			}
		}).open();
	}
</script>
<script type="text/javascript">
	window.history.forward();
	function noBack() {
		window.history.forward();
	}
</script>
<style>
* {
	box-sizing: border-box;
}

.total {
	min-width: 1180px !important;
	max-width: 1260px;
	padding: 0 50px;
	margin: 0 auto;
}

.table_st {
	border-width: 2px 0 0;
	border-style: solid;
	text-align: center;
	margin: auto;
	width: 100%;
}

.text-left {
	text-align: left;
}

.text-conter {
	text-align: center;
}

.table_st th, .table_st td {
	border-width: 0 0 1px;
	border-style: solid;
	border-color: #d0d0d0;
}

.intotal table {
	border-collapse: collapse;
}

.imggg {
	width: 140px;
	height: 140px;
}

#top {
	margin-top: 150px;
	width: 100%;
	text-align: center;
	font-size: 16px;
	line-height: 24px;
	color: black;
}

.a {
	width: 20%;
	padding: 10px;
	font-size: 15px;
}

.b {
	border: lightgray 1px solid;
	width: 300px;
	height: 30px;
	margin-top: 10px;
	margin-bottom: 10px;
}

.c {
	margin-top: 5px;
	margin-bottom: 10px;
}

.table_new {
	border-width: 2px 0 0;
	border-style: solid;
	margin: auto;
	width: 100%;
	height: 300px;
}

.table_new th, .table_new td {
	border-width: 0 0 1px;
	border-style: solid;
	border-color: #d0d0d0;
}

.phone>input {
	height: 37px;
	width: 80px;
	border: none;
	text-align: center;
}

.phone {
	border: lightgray 1px solid;
	width: 100px;
	height: 30px;
	margin-top: 10px;
	margin-bottom: 10px;
}

.box {
	margin: auto;
	float: left;
	padding: 30px;
	margin-left: 10px;
	text-align: center;
	width: 150px;
	height: 150px;
}

.clearfix {
	margin-left: 40%;
	float: left;
}

.clearfix::after {
	content: ""; 
	display: table;
	clear: both;
}

}
a {
	text-decoration: none;
	color: black;
}

.btn-find {
	margin-top: 30px;
	border: none;
	background-color: black;
	color: #fff;
	height: 30px;
	width: 90px;
}

.intotal {
	width: 80%;
	margin-bottom: 100px;
}

.intotal::after {
	content: "";
	clear: both;
	display: table;
}

.top-table {
	float: left;
	text-align: left;
	margin: auto;
	width: 80%;
	border: 3px none;
	padding: 5px;
	color: #555;
	font-size: 22px;
}

.orderbox {
	float: right;
	border: none;
	width: 320px;
	position: fixed;
	top: 250px;
	right: 200px;
}

.dl {
	border: 2px solid black;
	padding: 30px 30px 28px;
}

.order-dt {
	float: left;
	width: 90px;
}

.order-dd {
	width: 166px;
	text-align: right;
	font-size: 18px;
}

.order-dt, .order-ul {
	font-size: 14px;
	color: #555;
}

.order-ul {
	list-style-type: none;
	padding: 0;
}

#orderprice {
	margin-top: 23px;
}

.orderbutton {
	width: 100%;
	height: 60px;
	padding: 2px 60px 0;
	font-size: 16px;
	line-height: 20px;
	border: 2px solid black;
	background-color: black;
	color: white;
}

.w150 {
	width: 150px;
}
</style>
</head>

<body>

	<div id="top">
		<span>01 쇼핑백 > </span><span style="font-weight: bold">02 주문결제</span><span>
			> 03 주문완료</span>
	</div>
	<form action="" method="post" standby="true">
		<div class="orderbox">
			<h4 style="font-size: 22px">
				결제 금액 / 총
				<c:choose>
					<c:when test="${not empty cartList}">
					${orderCount}
					<input name="quantity" type="hidden" value="${orderCount}">
					</c:when>
					<c:otherwise>
					1
					<input name="quantity" type="hidden" value="1">
					</c:otherwise>
				</c:choose>
				개
			</h4>
			<dl class="dl">
				<dt class="order-dt">주문금액</dt>
				<dd class="order-dd">${totalPrice}</dd>
				<dt class="order-dt">배송비</dt>
				<dd class="order-dd">0</dd>
				<div id="orderprice">
					<dt class="order-dt">총 결제금액</dt>
					<dd class="order-dd">${totalPrice}</dd>
					<input type="hidden" name="total_amount" value="${totalPrice}">
				</div>
			</dl>
			<ul class="order-ul">
				<li><span> <input type="checkbox" name="agree1" id=""
						required> <label for="" class="order-li"> 주문 상품
							정보에 동의(필수) </label>
				</span></li>
				<li><span> <input type="checkbox" name="agree2" id=""
						required> <label for="" class="order-li">결제대행 서비스
							이용을 위한 개인정보 제3자 제공 및 위탁 동의(필수)</label>
				</span></li>
			</ul>
			<a href="#"><button class="orderbutton">주문하기</button></a>
		</div>
		<div class="total">
			<div class="intotal">
				<div class="top-table">
					<h4>상품정보</h4>
				</div>
				<table class="table_st">
					<thead>
						<tr>
							<td></td>
							<td>상품정보</td>
							<td class="w150">주문금액</td>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${cartList != null}">
								<c:forEach var="c" items="${cartList}">
									<input name="c_id" type="hidden" value="${c.cart_id }">
									<input name="item_name" type="hidden"
										value="${c.product_name }">
									<tr>
										<td class="imggg"><a href="#" class="img"> <img
												src="http://placehold.it/100"></a></td>
										<td class="text-left"><a href="#"><span>${c.product_seller_id}</span></a>
											<a href="#"><span>${c.product_name}</span></a>
											<div>
												<fmt:formatNumber value="${c.product_price}"
													pattern="#,###.##" />
											</div>
											<div>${c.sizes}</div>
											<div>수량: ${c.quantity} 개</div></td>
										<td class="text-center w150"><fmt:formatNumber
												value="${c.product_price * c.quantity}" pattern="#,###.##" /></td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td class="imggg"><a href="#" class="img"> <img
											src="http://placehold.it/100"></a></td>
									<td class="text-left"><a href="#"><span>${productDto.seller_id}</span></a>
										<a href="#"><span>${productDto.name}</span></a>
										<div>
											<input type="hidden" name="total_price"
												value="${productDto.price * quantity}">
											<fmt:formatNumber value="${productDto.price}"
												pattern="#,###.##" />
										</div>
										<div>${sizes}</div>
										<div>수량: ${quantity} 개</div></td>
									<td class="text-center w150"><fmt:formatNumber
											value="${productDto.price * quantity}" pattern="#,###.##" />
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
				<div class="top-table">
					<h4>주문하시는 분</h4>
				</div>
				<table class="table_new">
					<tbody id="old_body">
						<tr>
							<th class="a">이름</th>
							<td><input class="b name" type="text"
								value="${memberDto.name}"></td>
						</tr>
						<tr>
							<th class="a">전화번호</th>
							<td><c:set var="phone" value="${memberDto.phone }" /> <span><input
									type="text" value="${fn:substring(phone, 0,3)}" maxlength="3"
									required class="phone"></span> <span><input type="text"
									value="${fn:substring(phone, 3,7)}" maxlength="4" required
									class="phone"></span> <span><input type="text"
									value="${fn:substring(phone, 7,11)}" maxlength="4" required
									class="phone"></span></td>
						</tr>
						<tr>
							<th class="a">배송주소</th>
							<td>
								<div>
									<input type="text" id="post_code" readonly class="b"
										value="${memberDto.post_code}"> <input type="button"
										value="주소 찾기" required class="btn-find btn-addr"><br>
									<input type="text" id="basic_addr" readonly class="b"
										value="${memberDto.basic_addr}"> <input type="text"
										id="detail_addr" class="b" value="${memberDto.detail_addr}">
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="top-table">
					<h4>받으시는 분</h4>
				</div>
				<table class="table_new">
					<tbody id="new_body">
						<tr>
							<th class="a">배송지 선택</th>
							<td><input class="c" type="radio" name="select_info"
								id="same_info"><span style="font-size: 15px">&nbsp주문자
									정보와 동일</span> <input class="c" id="new_info" name="select_info"
								type="radio"><span style="font-size: 15px">&nbsp새로입력</span>
							</td>
						</tr>
						<tr>
							<th class="a">이름</th>
							<td><input class="b name" type="text" name="re_name"></td>
						</tr>
						<tr>
							<th class="a">전화번호</th>
							<td><span><input type="text" value maxlength="3"
									name="re_phones" required class="phone"></span> <span><input
									type="text" name="re_phones" value maxlength="4" required
									class="phone"></span> <span><input name="re_phones"
									type="text" value maxlength="4" required class="phone"></span></td>
						</tr>
						<tr>
							<th class="a">배송주소</th>
							<td>
								<div>
									<input type="text" name="post_code" id="post_code" readonly
										class="b"> <input type="button" value="주소 찾기" required
										class="btn-find btn-addr"><br> <input type="text"
										name="basic_addr" id="basic_addr" readonly class="b">
									<input type="text" name="detail_addr" id="detail_addr"
										class="b">
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="top-table">
					<h4>결제 정보</h4>
				</div>
				<div class="clearfix">
					<label for="kakao_pay" class="kakaopay box"
						style="border: #bbb solid 1px">
						<p>카카오 결제</p> <input type="radio" id="kakao_pay" name="pay_type"
						value="카카오페이">
					</label> <label for="deposit" class="pay box"
						style="border: #bbb solid 1px">
						<p>무통장입금</p> <input type="radio" id="deposit" name="pay_type"
						value="무통장입금">
					</label>
				</div>
			</div>
		</div>
	</form>
</body>
<jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
