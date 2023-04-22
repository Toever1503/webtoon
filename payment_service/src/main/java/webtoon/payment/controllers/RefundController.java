package webtoon.payment.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.entities.UserEntity;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.PaymentEntity;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.enums.EOrderType;
import webtoon.payment.enums.EPaymentMethod;
import webtoon.payment.services.IOrderService;
import webtoon.payment.services.IPaymentService;
import webtoon.payment.services.ISubscriptionPackService;
import webtoon.payment.models.OrderModel;

@Controller
@RequestMapping("payment/refund")
public class RefundController {
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private IOrderService orderService;

	@Autowired
	private ISubscriptionPackService subscriptionPackService;

	@PostMapping
	public void test(HttpServletRequest req, HttpServletResponse resp, @RequestParam int amount,
			@RequestParam String transNo) throws IOException {
		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_ResponseId", VnPayConfig.getRandomNumber(32)); // use uuid
		vnp_Params.put("vnp_Command", "refund");
		vnp_Params.put("vnp_Version", "2.1.0");
		vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
		vnp_Params.put("vnp_TransactionType", "02");
		vnp_Params.put("vnp_TxnRef", VnPayConfig.getRandomNumber(8));
		vnp_Params.put("vnp_Amount", String.valueOf(amount));
		vnp_Params.put("vnp_OrderInfo", "order info");
		vnp_Params.put("vnp_TransactionNo", transNo);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date dt = new Date();
		String vnp_CreateDate = formatter.format(dt);
		vnp_Params.put("vnp_TransactionDate	", vnp_CreateDate);
		vnp_Params.put("vnp_CreateDate	", vnp_CreateDate);
		vnp_Params.put("vnp_OrderInfo", "order info");
		vnp_Params.put("vnp_CreateBy", "admin user");
		vnp_Params.put("vnp_IpAddr", "0:0:0:0:0:0:0:1");

		List<String> fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);

		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator<String> itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) vnp_Params.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				// hashData.append(fieldValue); //sử dụng và 2.0.0 và 2.0.1 checksum sha256
				hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())); // sử dụng v2.1.0
																										// check sum
																										// sha512
				// Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
		System.out.println("hash: " + vnp_SecureHash);
		queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
		String paymentUrl = VnPayConfig.vnp_RefundUrl + "?" + queryUrl;

		resp.sendRedirect(paymentUrl);
	}

	@GetMapping("/ket-qua")
	public String ketQua(HttpServletRequest request, HttpServletResponse resp, Model model) throws ServletException, IOException, ParseException {
		Map fields = new HashMap();
		String vnp_IpAddr = VnPayConfig.getIpAddress(request);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		String vnp_ExpireDate = formatter.format(cld.getTime());
		for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
			String fieldName = (String) params.nextElement();
			String fieldValue = request.getParameter(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				fields.put(fieldName, fieldValue);
			}
		}

	;


		String vnp_SecureHash = request.getParameter("vnp_SecureHash");
		if (fields.containsKey("vnp_SecureHashType")) {
			fields.remove("vnp_SecureHashType");
		}
		if (fields.containsKey("vnp_SecureHash")) {
			fields.remove("vnp_SecureHash");
		}

		String signValue = VnPayConfig.hashAllFields(fields);
		String maDonHang = request.getParameter("vnp_TxnRef");
		String amount = request.getParameter("vnp_Amount");
		SubscriptionPackEntity subscriptionPack = subscriptionPackService.getByPrice(Double.parseDouble(amount)/100);
		Long id = orderService.getIdByMaDonHang(maDonHang);
		Long idPayment = paymentService.getIdByOrderId(id);
		String noiDungTT = request.getParameter("vnp_OrderInfo");
		String maPhanHoi = request.getParameter("vnp_ResponseCode");
		String maGD = request.getParameter("vnp_TransactionNo");
		String maNganHang = request.getParameter("vnp_BankCode");
		String thoiGianTT = request.getParameter("vnp_PayDate");
		String ketQua = "";
		amount = amount.substring(0, amount.length() - 2);
		StringBuilder query = new StringBuilder();
		StringBuilder hashData = new StringBuilder();
		String queryUrl = query.toString();
		String vnp_SecureHash1 = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
		System.out.println("hash: " + vnp_SecureHash1);
		queryUrl +="vnp_Amount="+amount+"vnp_TransactionNo="+maGD+"vnp_BankCode="+maNganHang+"vnp_PayDate="+thoiGianTT
//				+"&vnp_SecureHash=" + vnp_SecureHash
		;
		UserEntity user = SecurityUtils.getCurrentUser().getUser();
		String paymentUrl = VnPayConfig.vnp_Returnurl + "?" + queryUrl;
		if ("00".equals(maPhanHoi)) {
			ketQua = "Giao dịch thành công";
			orderService.update(new OrderModel(id, formatter.parse(thoiGianTT) , formatter.parse(thoiGianTT), Double.parseDouble(amount), EOrderType.EXTEND,"thanh toán", vnp_IpAddr, maDonHang,subscriptionPack, user, EPaymentMethod.VN_PAY));
			OrderEntity order = orderService.getMaDonHang(maDonHang);
//			paymentService.add(new PaymentEntity(Long.parseLong(maDonHang), order , maGD , maPhanHoi ,Double.parseDouble(amount) , maNganHang, noiDungTT,paymentUrl, formatter.parse(vnp_ExpireDate)));
			paymentService.update(new PaymentEntity(idPayment, order , maGD , maPhanHoi ,Double.parseDouble(amount) , maNganHang, 00, maNganHang, paymentUrl , formatter.parse(vnp_ExpireDate)));

		}else {
			ketQua = "Giao dịch không thành thành công";
		}

//		System.out.println(signValue);
		System.out.println(vnp_SecureHash);
		model.addAttribute("maDonHang", maDonHang);
		model.addAttribute("soTien", amount);
		model.addAttribute("noiDungTT", noiDungTT);
		model.addAttribute("maPhanHoi", maPhanHoi);
		model.addAttribute("maGD", maGD);
		model.addAttribute("maNganHang", maNganHang);
		model.addAttribute("thoiGianTT", thoiGianTT);
		model.addAttribute("ketQua", ketQua);
		return "payments/order_detail";
	}
}
