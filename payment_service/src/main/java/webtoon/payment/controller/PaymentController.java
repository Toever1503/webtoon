package webtoon.payment.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("payment/pay")
public class PaymentController {

	@GetMapping
	public void test(HttpServletRequest req, HttpServletResponse resp, @RequestParam Integer amount) throws IOException {
		String vnp_OrderInfo = "order info";
		String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
		String bank_code = ""; // edit later

		String vnp_Version = "2.1.0";
		String vnp_Command = "pay";
		String orderType = "ATM";
		String vnp_IpAddr = "0:0:0:0:0:0:0:1";
		String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

		amount = amount * 100;

		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_Version", vnp_Version);
		vnp_Params.put("vnp_Command", vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount));
		vnp_Params.put("vnp_CurrCode", "VND");

		if (bank_code != null && !bank_code.isEmpty()) {
			vnp_Params.put("vnp_BankCode", bank_code);
		}
		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
		vnp_Params.put("vnp_OrderType", orderType);

		String locate = "vn";
		if (locate != null && !locate.isEmpty()) {
			vnp_Params.put("vnp_Locale", locate);
		} else {
			vnp_Params.put("vnp_Locale", "vn");
		}
		vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_Returnurl);
		vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

		Date dt = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(dt);
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		Calendar cldvnp_ExpireDate = Calendar.getInstance();
		cldvnp_ExpireDate.add(Calendar.MINUTE, 30);
		Date vnp_ExpireDateD = cldvnp_ExpireDate.getTime();
		String vnp_ExpireDate = formatter.format(vnp_ExpireDateD);

		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

		List fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator itr = fieldNames.iterator();
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
		String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

		resp.sendRedirect(paymentUrl);
	}
}
