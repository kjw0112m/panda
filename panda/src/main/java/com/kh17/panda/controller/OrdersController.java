package com.kh17.panda.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh17.panda.entity.CartViewDto;
import com.kh17.panda.entity.OrderViewDto;
import com.kh17.panda.entity.OrdersDto;
import com.kh17.panda.entity.ProductDto;
import com.kh17.panda.repository.CartDao;
import com.kh17.panda.repository.MemberDao;
import com.kh17.panda.repository.OrdersDao;
import com.kh17.panda.repository.ProductDao;
import com.kh17.panda.vo.OrderAddressVO;
import com.kh17.panda.vo.OrderListVO;
import com.kh17.panda.vo.OrderVO;

@Controller
@RequestMapping("/orders")
public class OrdersController {

	@Autowired
	private OrdersDao ordersDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private CartDao cartDao;

	@Autowired
	private MemberDao memberDao;

	@GetMapping("/result")
	public String result() {
		return "orders/result";
	}

	@GetMapping("/order")
	public String order(@RequestParam(required = false, defaultValue = "0") int product_id,
			@RequestParam(required = false) String[] s_q, @RequestParam(required = false) int[] id,
			@RequestParam String totalPrice, HttpSession session, Model model) {
		String member_id = (String) session.getAttribute("sid");
		List<OrderVO> voList = new ArrayList<>();
		if (product_id > 0) {
			ProductDto productDto = productDao.get(product_id);
			for(String s : s_q) {
				String size = s.split("-")[0];
				int quantity = Integer.parseInt(s.split("-")[1]);
				voList.add(OrderVO.builder().productDto(productDto).size(size).quantity(quantity).build());
			}
			session.setAttribute("orderVO", voList);
			model.addAttribute("orderCount", voList.size());
		} else {
			List<CartViewDto> list = cartDao.list(id);
			model.addAttribute("cartList", list);
			model.addAttribute("orderCount", list.size());
		}
		model.addAttribute("memberDto", memberDao.get(member_id));
		model.addAttribute("totalPrice", totalPrice);
		return "orders/order";
	}

	@PostMapping("/order")
	public String order(@ModelAttribute OrdersDto ordersDto, @RequestParam(required = false) int[] c_id,
			@ModelAttribute OrderAddressVO orderAddressVO, @RequestParam(required = false) String[] re_phones,
			Model model, HttpSession session) {
		StringBuffer re_phone = new StringBuffer();
		for (String s : re_phones) {
			re_phone.append(s);
		}

		// 기본 DTO 설정
		ordersDto.setMember_id((String) session.getAttribute("sid"));
		if (ordersDto.getPay_type().equals("카카오페이")) {
			ordersDto.setPay_status("결제완료");
			ordersDto.setT_status("배송준비중");
		} else if (ordersDto.getPay_type().equals("무통장입금"))
			ordersDto.setPay_status("입금전");
		ordersDto.setRe_phone(re_phone.toString());
		ordersDto.setRe_addr("[" + orderAddressVO.getPost_code() + "]" + orderAddressVO.getBasic_addr()
				+ orderAddressVO.getDetail_addr());

		int count = 0;
		String team = null;
//		장바구니에서 주문할 경우
		if (c_id != null) { 
			List<CartViewDto> list = cartDao.list(c_id);

			for (CartViewDto cartViewDto : list) {
				ordersDto.setId(ordersDao.seq());
				ordersDto.setQuantity(cartViewDto.getQuantity());
				ordersDto.setTotal_price(cartViewDto.getQuantity() * cartViewDto.getProduct_price());
				ordersDto.setSizes(cartViewDto.getSizes());
				ordersDto.setProduct_id(cartViewDto.getProduct_id());

				if (count > 0) {
					ordersDto.setTeam(team);
				}

				ordersDao.insert(ordersDto);
				if (count == 0) {
					team = ordersDao.getOrderId(ordersDto.getId());
				}
				count++;
				cartDao.delete(cartViewDto.getCart_id());
			}
		}
//		상품 상세화면에서 단일 주문할 경우
		else {
			@SuppressWarnings("unchecked")
			List<OrderVO> voList = (List<OrderVO>) session.getAttribute("orderVO");
			
			for(OrderVO orderVO : voList) {
				ordersDto.setId(ordersDao.seq());
				ordersDto.setQuantity(orderVO.getQuantity());
				ordersDto.setTotal_price(orderVO.getQuantity() * orderVO.getProductDto().getPrice());
				ordersDto.setSizes(orderVO.getSize());
				ordersDto.setProduct_id(orderVO.getProductDto().getId());

				if (count > 0) {
					ordersDto.setTeam(team);
				}

				ordersDao.insert(ordersDto);
				if (count == 0) {
					team = ordersDao.getOrderId(ordersDto.getId());
				}
				count++;
			}
		}
		session.removeAttribute("orderVO");
		return "orders/result";
	}

	@GetMapping("/list")
	public String list(@ModelAttribute OrderViewDto orderViewDto, Model model,
			@RequestParam(required = false, defaultValue = "1") int page, HttpSession session) {

		String member_id = (String) session.getAttribute("sid");
		orderViewDto.setMember_id(member_id);
		int pagesize = 5;
		int start = pagesize * page - (pagesize - 1);
		int end = pagesize * page;

		int blocksize = 10;
		int startBlock = (page - 1) / blocksize * blocksize + 1;
		int endBlock = startBlock + (blocksize - 1);

		int count = ordersDao.count(orderViewDto);
		int pageCount = (count - 1) / pagesize + 1;
		if (endBlock > pageCount) {
			endBlock = pageCount;
		}

		List<OrderListVO> list = ordersDao.list(orderViewDto, start, end);
		model.addAttribute("page", page);
		model.addAttribute("startBlock", startBlock);
		model.addAttribute("endBlock", endBlock);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("myOrder", list);

		return "orders/list";
	}
	
	@GetMapping("/stat_list")
	public String statList(@ModelAttribute OrderViewDto orderViewDto, Model model,
			@RequestParam(required = false, defaultValue = "1") int page, HttpSession session) {
		
		String member_id = (String) session.getAttribute("sid");
		orderViewDto.setMember_id(member_id);
		int pagesize = 5;
		int start = pagesize * page - (pagesize - 1);
		int end = pagesize * page;
		
		int blocksize = 10;
		int startBlock = (page - 1) / blocksize * blocksize + 1;
		int endBlock = startBlock + (blocksize - 1);
		
		int count = ordersDao.statCount(orderViewDto);
		int pageCount = (count - 1) / pagesize + 1;
		if (endBlock > pageCount) {
			endBlock = pageCount;
		}
		
		List<OrderListVO> list = ordersDao.statList(orderViewDto, start, end);
		model.addAttribute("page", page);
		model.addAttribute("startBlock", startBlock);
		model.addAttribute("endBlock", endBlock);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("myOrder", list);
		
		return "orders/stat_list";
	}

	@GetMapping("/detail/{team}")
	public String detail(@PathVariable String team, Model model, HttpSession session) {
		String id = (String) session.getAttribute("sid");

		if (id != null) {
			List<OrderViewDto> list = ordersDao.list(team);
			int price = 0;
			for (OrderViewDto dto : list) {
				price += dto.getTotal_price();
			}

			model.addAttribute("price", price);
			model.addAttribute("orderViewDto", list);
		}
		return "orders/detail";
	}

	@GetMapping("/csList")
	public String csList(@ModelAttribute OrderViewDto orderViewDto, RedirectAttributes model,
			@RequestParam(required = false, defaultValue = "1") int page) {
		return "redirect:csList";
	}

	@GetMapping("/cancel/{team}")
	public String cancel(@PathVariable String team, Model model, HttpSession session) {
		String id = (String) session.getAttribute("sid");

		if (id != null) {
			List<OrderViewDto> list = ordersDao.list(team);

			model.addAttribute("orderViewDto", list);
		}
		return "orders/cancel";
	}

	@GetMapping("/exchange/{team}")
	public String exchange(@PathVariable String team, Model model, HttpSession session) {
		String id = (String) session.getAttribute("sid");

		if (id != null) {
			List<OrderViewDto> list = ordersDao.list(team);

			model.addAttribute("orderViewDto", list);
		}
		return "orders/exchange";
	}

	@GetMapping("/return/{team}")
	public String returns(@PathVariable String team, Model model, HttpSession session) {
		String id = (String) session.getAttribute("sid");

		if (id != null) {
			List<OrderViewDto> list = ordersDao.list(team);

			model.addAttribute("orderViewDto", list);
		}
		return "orders/return";
	}

	@PostMapping("/cancel")
	public String cancel(@RequestParam String[] order_id, @RequestParam String pay_status, Model model,
			HttpSession session) {
		String id = (String) session.getAttribute("sid");
		if (id != null) {
			for (String order : order_id) {
				if (pay_status.equals("무통장입금"))
					ordersDao.cs_change(OrdersDto.builder().cs_status("환불").order_id(order).build());
				else
					ordersDao.cs_change(OrdersDto.builder().cs_status("취소").order_id(order).build());
				ordersDao.t_change(OrdersDto.builder().t_status("").order_id(order).build());
				ordersDao.pay_change(OrdersDto.builder().pay_status("").order_id(order).build());
			}
		}
		return "redirect:list";
	}

	@PostMapping("/return")
	public String returns(@RequestParam String[] order_id, @RequestParam String pay_status, Model model,
			HttpSession session) {
		String id = (String) session.getAttribute("sid");
		if (id != null) {
			for (String order : order_id) {
				ordersDao.cs_change(OrdersDto.builder().cs_status("반품").order_id(order).build());
				ordersDao.t_change(OrdersDto.builder().t_status("").order_id(order).build());
				ordersDao.pay_change(OrdersDto.builder().pay_status("").order_id(order).build());
			}
		}
		return "redirect:list";
	}

	@PostMapping("/exchange")
	public String exchange(@RequestParam String[] order_id, @RequestParam String pay_status, Model model,
			HttpSession session) {
		String id = (String) session.getAttribute("sid");
		if (id != null) {
			for (String order : order_id) {
				ordersDao.cs_change(OrdersDto.builder().cs_status("교환").order_id(order).build());
				ordersDao.t_change(OrdersDto.builder().t_status("").order_id(order).build());
				ordersDao.pay_change(OrdersDto.builder().pay_status("").order_id(order).build());
			}
		}
		return "redirect:list";
	}
}
