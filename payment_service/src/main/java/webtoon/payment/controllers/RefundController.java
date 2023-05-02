package webtoon.payment.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import javax.mail.MessagingException;
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
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.enums.EOrderType;
import webtoon.payment.enums.EPaymentMethod;
import webtoon.payment.services.IOrderService;
import webtoon.payment.services.IPaymentService;
import webtoon.payment.services.ISendEmail;
import webtoon.payment.services.ISubscriptionPackService;
import webtoon.payment.models.OrderModel;
import webtoon.payment.services.impl.SendEmailServiceImpl;

@Controller
@RequestMapping("payment/refund")
public class RefundController {

	@Autowired
	private ISendEmail sendEmail;
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
	public String ketQua(HttpServletRequest request, HttpServletResponse resp, Model model) throws ServletException, IOException, ParseException, MessagingException {
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

		if (fields.containsKey("vnp_SecureHashType")) {
			fields.remove("vnp_SecureHashType");
		}
		if (fields.containsKey("vnp_SecureHash")) {
			fields.remove("vnp_SecureHash");
		}
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		String queryUrl = query.toString();
		String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
		System.out.println("hash: " + vnp_SecureHash);
		queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
		String paymentUrl = VnPayConfig.vnp_RefundUrl + "?" + queryUrl;



		String signValue = VnPayConfig.hashAllFields(fields);
		String maDonHang = request.getParameter("vnp_TxnRef");
		String amount = request.getParameter("vnp_Amount");
		SubscriptionPackEntity subscriptionPack = subscriptionPackService.getByPrice(Double.parseDouble(amount)/100);
		Calendar cldvnp_ExpireDate = Calendar.getInstance();
		cldvnp_ExpireDate.add(Calendar.DAY_OF_MONTH,subscriptionPack.getDayCount());
		Date subExpireDate = cldvnp_ExpireDate.getTime();
		Long id = orderService.getIdByMaDonHang(maDonHang);
		Long idPayment = paymentService.getIdByOrderId(id);
		String noiDungTT = request.getParameter("vnp_OrderInfo");
		String maPhanHoi = request.getParameter("vnp_ResponseCode");
		String maGD = request.getParameter("vnp_TransactionNo");
		String maNganHang = request.getParameter("vnp_BankCode");
		String thoiGianTT = request.getParameter("vnp_PayDate");
		String ketQua = "";
		amount = amount.substring(0, amount.length() - 2);

		UserEntity user = SecurityUtils.getCurrentUser().getUser();
		String email = user.getEmail();
		if ("00".equals(maPhanHoi)) {
			ketQua = "Giao dịch thành công";
			orderService.add(new OrderModel(Long.parseLong(maDonHang),formatter.parse(thoiGianTT),formatter.parse(thoiGianTT) , subExpireDate, Double.parseDouble((amount)), EOrderType.NEW, EOrderStatus.COMPLETED ,"thanh toán", vnp_IpAddr,maDonHang,subscriptionPack, user, EPaymentMethod.VN_PAY));
			OrderEntity order = orderService.getMaDonHang(maDonHang);
//			paymentService.add(new PaymentEntity(Long.parseLong(maDonHang), order , maGD , maPhanHoi ,Double.parseDouble(amount) , maNganHang, noiDungTT,paymentUrl, formatter.parse(vnp_ExpireDate)));
			paymentService.add(new PaymentEntity(Long.parseLong(maDonHang), order , maGD , maPhanHoi ,Double.parseDouble(amount) , maNganHang, 00, maNganHang, paymentUrl , formatter.parse(vnp_ExpireDate)));
//			sendEmail.sendingPayment(email);
			sendEmail.sendEmail(email, "Thanh toán thành công", "\n" +
					"<html lang=\"en\" >\n" +
					"\n" +
					"<head>\n" +
					"    <meta charset=\"UTF-8\">\n" +
					"    <meta name=\"viewport\"\n" +
					"        content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
					"    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
					"    <title>Document</title>\n" +
					"\n" +
					"    <style type=\"text/css\">\n" +
					"        @media only screen and (min-width: 620px) {\n" +
					"            .u-row {\n" +
					"                width: 600px !important;\n" +
					"            }\n" +
					"\n" +
					"            .u-row .u-col {\n" +
					"                vertical-align: top;\n" +
					"            }\n" +
					"\n" +
					"            .u-row .u-col-50 {\n" +
					"                width: 300px !important;\n" +
					"            }\n" +
					"\n" +
					"            .u-row .u-col-100 {\n" +
					"                width: 600px !important;\n" +
					"            }\n" +
					"\n" +
					"        }\n" +
					"\n" +
					"        @media (max-width: 620px) {\n" +
					"            .u-row-container {\n" +
					"                max-width: 100% !important;\n" +
					"                padding-left: 0px !important;\n" +
					"                padding-right: 0px !important;\n" +
					"            }\n" +
					"\n" +
					"            .u-row .u-col {\n" +
					"                min-width: 320px !important;\n" +
					"                max-width: 100% !important;\n" +
					"                display: block !important;\n" +
					"            }\n" +
					"\n" +
					"            .u-row {\n" +
					"                width: calc(100% - 40px) !important;\n" +
					"            }\n" +
					"\n" +
					"            .u-col {\n" +
					"                width: 100% !important;\n" +
					"            }\n" +
					"\n" +
					"            .u-col>div {\n" +
					"                margin: 0 auto;\n" +
					"            }\n" +
					"        }\n" +
					"\n" +
					"        body {\n" +
					"            margin: 0;\n" +
					"            padding: 0;\n" +
					"        }\n" +
					"\n" +
					"        table,\n" +
					"        tr,\n" +
					"        td {\n" +
					"            vertical-align: top;\n" +
					"            border-collapse: collapse;\n" +
					"        }\n" +
					"\n" +
					"        p {\n" +
					"            margin: 0;\n" +
					"        }\n" +
					"\n" +
					"        .ie-container table,\n" +
					"        .mso-container table {\n" +
					"            table-layout: fixed;\n" +
					"        }\n" +
					"\n" +
					"        * {\n" +
					"            line-height: inherit;\n" +
					"        }\n" +
					"\n" +
					"        a[x-apple-data-detectors='true'] {\n" +
					"            color: inherit !important;\n" +
					"            text-decoration: none !important;\n" +
					"        }\n" +
					"\n" +
					"        table,\n" +
					"        td {\n" +
					"            color: #000000;\n" +
					"        }\n" +
					"\n" +
					"        a {\n" +
					"            color: #ff6633;\n" +
					"            text-decoration: underline;\n" +
					"        }\n" +
					"    </style>\n" +
					"\n" +
					"\n" +
					"\n" +
					"    <!--[if !mso]><!-->\n" +
					"    <link href=\"https://fonts.googleapis.com/css?family=Lato:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\">\n" +
					"    <!--<![endif]-->\n" +
					"\n" +
					"</head>\n" +
					"\n" +
					"<body class=\"clean-body u_body\"\n" +
					"    style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #ffffff;color: #000000\">\n" +
					"   <table\n" +
					"        style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #ffffff;width:100%\"\n" +
					"        cellpadding=\"0\" cellspacing=\"0\">\n" +
					"        <tbody>\n" +
					"            <tr style=\"vertical-align: top\">\n" +
					"                <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\n" +
					"                <div class=\"u-row-container\" style=\"padding: 0px;background-color: #f9f9f9\">\n" +
					"                        <div class=\"u-row\"\n" +
					"                            style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #f9f9f9;\">\n" +
					"                            <div\n" +
					"                                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
					"                             <div class=\"u-col u-col-100\"\n" +
					"                                    style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
					"                                    <div style=\"height: 100%;width: 100% !important;\">\n" +
					"                                        <div\n" +
					"                                            style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
					"                                          <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:15px;font-family:'Lato',sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <table height=\"0px\" align=\"center\" border=\"0\"\n" +
					"                                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
					"                                                                style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #f9f9f9;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
					"                                                                <tbody>\n" +
					"                                                                    <tr style=\"vertical-align: top\">\n" +
					"                                                                        <td\n" +
					"                                                                            style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
					"                                                                            <span>&#160;</span>\n" +
					"                                                                        </td>\n" +
					"                                                                    </tr>\n" +
					"                                                                </tbody>\n" +
					"                                                            </table>\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"                                        </div>\n" +
					"                                    </div>\n" +
					"                                </div>\n" +
					"                            </div>\n" +
					"                        </div>\n" +
					"                    </div>\n" +
					"                    <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
					"                        <div class=\"u-row\"\n" +
					"                            style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\n" +
					"                            <div\n" +
					"                                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
					"                               <div class=\"u-col u-col-100\"\n" +
					"                                    style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
					"                                    <div style=\"height: 100%;width: 100% !important;\">\n" +
					"                                        \n" +
					"                                        <div\n" +
					"                                            style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
					"                                          <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:25px 10px;font-family:'Lato',sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"\n" +
					"                                                                border=\"0\">\n" +
					"                                                                <tr>\n" +
					"                                                                    <td style=\"padding-right: 0px;padding-left: 0px;\"\n" +
					"                                                                        align=\"center\">\n" +
					"\n" +
					"                                                                        <img align=\"center\" border=\"0\"\n" +
					"                                                                            src=\"https://team-2.s3.ap-northeast-2.amazonaws.com/static-images/imagse-5.png\"\n" +
					"                                                                            alt=\"Image\" title=\"Image\"\n" +
					"                                                                            style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 29%;max-width: 168.2px;\"\n" +
					"                                                                            width=\"168.2\" />\n" +
					"\n" +
					"                                                                    </td>\n" +
					"                                                                </tr>\n" +
					"                                                            </table>\n" +
					"\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"                                        </div>\n" +
					"                                    </div>\n" +
					"                                </div>\n" +
					"                            </div>\n" +
					"                        </div>\n" +
					"                    </div>\n" +
					"                    <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
					"                        <div class=\"u-row\"\n" +
					"                            style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #638fa3;\">\n" +
					"                            <div\n" +
					"                                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
					"                             <div class=\"u-col u-col-100\"\n" +
					"                                    style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
					"                                    <div style=\"height: 100%;width: 100% !important;\">\n" +
					"                                        <div\n" +
					"                                            style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
					"                                          <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:35px 10px 10px;font-family:'Lato',sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"\n" +
					"                                                                border=\"0\">\n" +
					"                                                                <tr>\n" +
					"                                                                    <td style=\"padding-right: 0px;padding-left: 0px;\"\n" +
					"                                                                        align=\"center\">\n" +
					"\n" +
					"                                                                        <img align=\"center\" border=\"0\"\n" +
					"                                                                            src=\"https://team-2.s3.ap-northeast-2.amazonaws.com/static-images/shopping-cart.png\"\n" +
					"                                                                            alt=\"Image\" title=\"Image\"\n" +
					"                                                                            style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 15%;max-width: 87px;\"\n" +
					"                                                                            width=\"87\" />\n" +
					"\n" +
					"                                                                    </td>\n" +
					"                                                                </tr>\n" +
					"                                                            </table>\n" +
					"\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"\n" +
					"                                            <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:0px 10px 30px;font-family:'Lato',sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <div\n" +
					"                                                                style=\"line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
					"                                                                <p\n" +
					"                                                                    style=\"font-size: 14px; line-height: 140%; text-align: center;\">\n" +
					"                                                                    <span\n" +
					"                                                                        style=\"font-size: 28px; line-height: 39.2px; color: #ffffff; font-family: Tahoma, sans-serif;\">\n" +
					"                                                                        Bạn đã thanh toán thành công</span>\n" +
					"                                                                </p>\n" +
					"                                                            </div>\n" +
					"\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"                                        </div>\n" +
					"                                    </div>\n" +
					"                                </div>\n" +
					"                            </div>\n" +
					"                        </div>\n" +
					"                    </div>\n" +
					"                    <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
					"                        <div class=\"u-row\"\n" +
					"                            style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
					"                            <div\n" +
					"                                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
					"                               <div class=\"u-col u-col-100\"\n" +
					"                                    style=\"max-width: 320px;min-width: 500px;display: table-cell;vertical-align: top;\">\n" +
					"                                    <div\n" +
					"                                        style=\"background-color: #ffffff;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
					"                                       <div\n" +
					"                                            style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
					"                                           <table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 10px 10px;font-family:arial,helvetica,sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <div\n" +
					"                                                                style=\"line-height: 140%; text-align: center; word-wrap: break-word;\">\n" +
					"                                                                <p style=\"font-size: 14px; line-height: 140%;\">\n" +
					"                                                                    <strong><span\n" +
					"                                                                            style=\"font-family: Montserrat, sans-serif; font-size: 16px; line-height: 22.4px;\">Tóm\n" +
					"                                                                            tắt đơn hàng</span></strong>\n" +
					"                                                                </p>\n" +
					"                                                            </div>\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"                                    <table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <table height=\"0px\" align=\"center\" border=\"0\"\n" +
					"                                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
					"                                                                style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 2px solid #e7e7e7;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
					"                                                                <tbody>\n" +
					"                                                                    <tr style=\"vertical-align: top\">\n" +
					"                                                                        <td\n" +
					"                                                                            style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
					"                                                                            <span>ABC</span>\n" +
					"                                                                           \n" +
					"                                                                        </td>\n" +
					"                                                                    </tr>\n" +
					"                                                                </tbody>\n" +
					"                                                            </table>\n" +
					"\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"                                        </div>\n" +
					"                                    </div>\n" +
					"                                </div>\n" +
					"                            </div>\n" +
					"                        </div>\n" +
					"                    </div>\n" +
					"                    <div th:each=\"item : ${order.getOrderDetails()}\">\n" +
					"                        <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
					"                            <div class=\"u-row\"\n" +
					"                                style=\"Margin: 0 auto;min-width: 350px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
					"                                <div\n" +
					"                                    style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
					"                                    <div class=\"u-col u-col-23p14\"\n" +
					"                                        style=\"max-width: 320px;min-width: 116px;display: table-cell;vertical-align: top;\">\n" +
					"                                        <div\n" +
					"                                            style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
					"                                           <div\n" +
					"                                                style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
					"                                                <table style=\"font-family:arial,helvetica,sans-serif;\"\n" +
					"                                                    role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
					"                                                    border=\"0\">\n" +
					"                                                    <tbody>\n" +
					"                                                        <tr>\n" +
					"                                                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:0px;font-family:arial,helvetica,sans-serif;\"\n" +
					"                                                                align=\"left\">\n" +
					"\n" +
					"                                                                <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"\n" +
					"                                                                    border=\"0\">\n" +
					"                                                                    <tr>\n" +
					"                                                                        <td style=\"padding-right: 0px;padding-left: 0px;\"\n" +
					"                                                                            align=\"left\">\n" +
					"                                                                            <div>\n" +
					"                                                                              <strong><span\n" +
					"                                                                            style=\"font-family: Montserrat, sans-serif; font-size: 14px; line-height: 19.6px;\">Bạn\n" +
					"                                                                            đã mua gói: \n" + subscriptionPack.getName() +
					"                                                                        </span></strong>\n" +
					"                                                                            </div>\n" +
					"\n" +
					"                                                                        </td>\n" +
					"                                                                    </tr>\n" +
					"                                                                </table>\n" +
					"                                                            </td>\n" +
					"                                                        </tr>\n" +
					"                                                    </tbody>\n" +
					"                                                </table>\n" +
					"                                            </div>\n" +
					"                                        </div>\n" +
					"                                    </div>\n" +
					"                                </div>\n" +
					"                            </div>\n" +
					"                        </div>\n" +
					"                    </div>\n" +
					"                    <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
					"                        <div class=\"u-row\"\n" +
					"                            style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
					"                            <div\n" +
					"                                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
					"                              <div class=\"u-col u-col-23p13\"\n" +
					"                                    style=\"max-width: 320px;min-width: 116px;display: table-cell;vertical-align: top;\">\n" +
					"                                    <div\n" +
					"                                        style=\"background-color: #ffffff;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
					"                                      <div\n" +
					"                                            style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
					"                                         <table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 2px;font-family:arial,helvetica,sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <div\n" +
					"                                                                style=\"line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
					"                                                                <p style=\"font-size: 14px; line-height: 140%;\">\n" +
					"                                                                    <strong><span\n" +
					"                                                                            style=\"font-family: Montserrat, sans-serif; font-size: 14px; line-height: 19.6px;\">Phương\n" +
					"                                                                            thức thanh toán:\n" + EPaymentMethod.VN_PAY +
					"                                                                        </span></strong>\n" +
					"                                                                </p>\n" +
					"                                                            </div>\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 2px;font-family:arial,helvetica,sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <div\n" +
					"                                                                style=\"line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
					"                                                                <div\n" +
					"                                                                    style=\"line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
					"                                                                    <p style=\"font-size: 14px; line-height: 140%;\"><span\n" +
					"                                                                            style=\"font-family: Montserrat, sans-serif;\"><strong>\n" +
					"                                                                                Tổng tiền: " + amount +"VNĐ" +
					"																				</strong></span></p>\n"  +
					"                                                                    </strong></span></p>\n" +
					"                                                                </div>\n" +
					"                                                            </div>\n" +
					"\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"                                        </div>\n" +
					"                                    </div>\n" +
					"                                </div>\n" +
					"                    <div class=\"u-col u-col-22p74\"style=\"max-width: 320px;min-width: 114px;display: table-cell;vertical-align: top;\">\n" +
					"                                    <div\n" +
					"                                        style=\"background-color: #ffffff;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
					"                                     <div\n" +
					"                                            style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
					"                                        <table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <div\n" +
					"                                                                style=\"line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
					"                                                                <p style=\"font-size: 14px; line-height: 140%;\"><span\n" +
					"                                                                        style=\"font-family: Montserrat, sans-serif; font-size: 14px; line-height: 19.6px;\"\n" +
					"                                                                        th:text=\"${formatter.format(totalPrices - deliveryFee)}\"></span>\n" +
					"                                                                </p>\n" +
					"                                                                <p style=\"font-size: 14px; line-height: 140%;\"><span\n" +
					"                                                                        style=\"font-family: Montserrat, sans-serif; font-size: 14px; line-height: 19.6px;\"\n" +
					"                                                                        th:text=\"${formatter.format(deliveryFee)}\"></span>\n" +
					"                                                                </p>\n" +
					"                                                            </div>\n" +
					"\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"                                        </div>\n" +
					"                                    </div>\n" +
					"                                </div>\n" +
					"                            </div>\n" +
					"                        </div>\n" +
					"                    </div>\n" +
					"                    </div>\n" +
					"                    <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
					"                        <div class=\"u-row\"\n" +
					"                            style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\n" +
					"                            <div\n" +
					"                                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
					"                              <div class=\"u-col u-col-100\"\n" +
					"                                    style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
					"                                    <div style=\"height: 100%;width: 100% !important;\">\n" +
					"                                       \n" +
					"                                        <div\n" +
					"                                            style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
					"                                           <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 40px 30px;font-family:'Lato',sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <div\n" +
					"                                                                style=\"line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
					"                                                                <p style=\"font-size: 14px; line-height: 140%;\"><span\n" +
					"                                                                        style=\"font-family: Tahoma, sans-serif; font-size: 14px; line-height: 19.6px; color: #888888;\">Nếu\n" +
					"                                                                        bạn có bất kì câu hỏi nào, hãy trả lời lại email\n" +
					"                                                                        này hoặc <strong>liên hệ</strong> với chúng tôi\n" +
					"                                                                        theo địa chỉ bên dưới.</span></p>\n" +
					"                                                                <p style=\"font-size: 14px; line-height: 140%;\"><span\n" +
					"                                                                        style=\"font-family: Tahoma, sans-serif; font-size: 14px; line-height: 19.6px; color: #888888;\">\n" +
					"                                                                        Xin trân thành cảm ơn quý khách.</span></p>\n" +
					"                                                            </div>\n" +
					"\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"                                        </div>\n" +
					"                                    </div>\n" +
					"                                </div>\n" +
					"                            </div>\n" +
					"                        </div>\n" +
					"                    </div>\n" +
					"                    <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
					"                        <div class=\"u-row\"\n" +
					"                            style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #638fa3;\">\n" +
					"                            <div\n" +
					"                                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
					"                   <div class=\"u-col u-col-50\"\n" +
					"                                    style=\"max-width: 320px;min-width: 300px;display: table-cell;vertical-align: top;\">\n" +
					"                                    <div style=\"height: 100%;width: 100% !important;\">\n" +
					"                                        <div\n" +
					"                                            style=\"padding: 20px 20px 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
					"                                    <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Lato',sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <div\n" +
					"                                                                style=\"line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
					"                                                                <p style=\"font-size: 14px; line-height: 140%;\"><span\n" +
					"                                                                        style=\"font-size: 16px; line-height: 22.4px; color: #ecf0f1;\">Liên\n" +
					"                                                                        Hệ</span></p>\n" +
					"                                                                <p style=\"font-size: 14px; line-height: 140%;\"><span\n" +
					"                                                                        style=\"font-size: 14px; line-height: 19.6px; color: #ecf0f1;\">Cao\n" +
					"                                                                        dang fpt polytechnic</span></p>\n" +
					"                                                                <p style=\"font-size: 14px; line-height: 140%;\"><span\n" +
					"                                                                        style=\"font-size: 14px; line-height: 19.6px; color: #ecf0f1;\">Việt\n" +
					"                                                                        Nam</span></p>\n" +
					"                                                                <p style=\"font-size: 14px; line-height: 140%;\"><span\n" +
					"                                                                        style=\"font-size: 14px; line-height: 19.6px; color: #ecf0f1;\">+84\n" +
					"                                                                        987 888 888 | +84 987 999 999</span></p>\n" +
					"                                                                <p style=\"font-size: 14px; line-height: 140%;\"> </p>\n" +
					"                                                                <p style=\"font-size: 14px; line-height: 140%;\"><span\n" +
					"                                                                        style=\"font-size: 14px; line-height: 19.6px; color: #ecf0f1;\">\n" +
					"                                                                        fpt@gmail.com</span></p>\n" +
					"                                                            </div>\n" +
					"\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"                                        </div>\n" +
					"                                    </div>\n" +
					"                                </div>\n" +
					"                              <div class=\"u-col u-col-50\"\n" +
					"                                    style=\"max-width: 320px;min-width: 300px;display: table-cell;vertical-align: top;\">\n" +
					"                                    <div style=\"height: 100%;width: 100% !important;\">\n" +
					"                                        <div\n" +
					"                                            style=\"padding: 0px 0px 0px 20px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
					"                                          <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:25px 10px 10px;font-family:'Lato',sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <div align=\"left\">\n" +
					"                                                                <div style=\"display: table; max-width:187px;\">\n" +
					"                                                                    <table align=\"left\" border=\"0\" cellspacing=\"0\"\n" +
					"                                                                        cellpadding=\"0\" width=\"32\" height=\"32\"\n" +
					"                                                                        style=\"width: 32px !important;height: 32px !important;display: inline-block;border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;margin-right: 15px\">\n" +
					"                                                                        <tbody>\n" +
					"                                                                            <tr style=\"vertical-align: top\">\n" +
					"                                                                                <td align=\"left\" valign=\"middle\"\n" +
					"                                                                                    style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\n" +
					"                                                                                    <a href=\" \" title=\"Facebook\"\n" +
					"                                                                                        target=\"_blank\">\n" +
					"                                                                                        <img src=\"https://team-2.s3.ap-northeast-2.amazonaws.com/static-images/image-2.png\"\n" +
					"                                                                                            alt=\"Facebook\"\n" +
					"                                                                                            title=\"Facebook\" width=\"32\"\n" +
					"                                                                                            style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: block !important;border: none;height: auto;float: none;max-width: 32px !important\">\n" +
					"                                                                                    </a>\n" +
					"                                                                                </td>\n" +
					"                                                                            </tr>\n" +
					"                                                                        </tbody>\n" +
					"                                                                    </table>\n" +
					"                                                                   <table align=\"left\" border=\"0\" cellspacing=\"0\"\n" +
					"                                                                        cellpadding=\"0\" width=\"32\" height=\"32\"\n" +
					"                                                                        style=\"width: 32px !important;height: 32px !important;display: inline-block;border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;margin-right: 15px\">\n" +
					"                                                                        <tbody>\n" +
					"                                                                            <tr style=\"vertical-align: top\">\n" +
					"                                                                                <td align=\"left\" valign=\"middle\"\n" +
					"                                                                                    style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\n" +
					"                                                                                    <a href=\" \" title=\"Instagram\"\n" +
					"                                                                                        target=\"_blank\">\n" +
					"                                                                                        <img src=\"https://team-2.s3.ap-northeast-2.amazonaws.com/static-images/image-3.png\"\n" +
					"                                                                                            alt=\"Instagram\"\n" +
					"                                                                                            title=\"Instagram\" width=\"32\"\n" +
					"                                                                                            style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: block !important;border: none;height: auto;float: none;max-width: 32px !important\">\n" +
					"                                                                                    </a>\n" +
					"                                                                                </td>\n" +
					"                                                                            </tr>\n" +
					"                                                                        </tbody>\n" +
					"                                                                    </table>\n" +
					"                                                                </div>\n" +
					"                                                            </div>\n" +
					"\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"\n" +
					"                                            <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:5px 10px 10px;font-family:'Lato',sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <div\n" +
					"                                                                style=\"line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
					"                                                                <p style=\"line-height: 140%; font-size: 14px;\"><span\n" +
					"                                                                        style=\"font-size: 14px; line-height: 19.6px;\"><span\n" +
					"                                                                            style=\"color: #ecf0f1; font-size: 14px; line-height: 19.6px;\"><span\n" +
					"                                                                                style=\"line-height: 19.6px; font-size: 14px;\">Fpt polytechnic ©  Mọi quyền được bảo\n" +
					"                                                                                lưu</span></span></span></p>\n" +
					"                                                            </div>\n" +
					"\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"                                        </div>\n" +
					"                                    </div>\n" +
					"                                </div>\n" +
					"                            </div>\n" +
					"                        </div>\n" +
					"                    </div>\n" +
					"                    <div class=\"u-row-container\" style=\"padding: 0px;background-color: #f9f9f9\">\n" +
					"                        <div class=\"u-row\"\n" +
					"                            style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #638fa3;\">\n" +
					"                            <div\n" +
					"                                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
					"                               <div class=\"u-col u-col-100\"\n" +
					"                                    style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
					"                                    <div style=\"height: 100%;width: 100% !important;\">\n" +
					"                                     \n" +
					"                                        <div\n" +
					"                                            style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
					"                                       \n" +
					"\n" +
					"                                            <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:15px;font-family:'Lato',sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <table height=\"0px\" align=\"center\" border=\"0\"\n" +
					"                                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
					"                                                                style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #f9f9f9;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
					"                                                                <tbody>\n" +
					"                                                                    <tr style=\"vertical-align: top\">\n" +
					"                                                                        <td\n" +
					"                                                                            style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
					"                                                                            <span>&#160;</span>\n" +
					"                                                                        </td>\n" +
					"                                                                    </tr>\n" +
					"                                                                </tbody>\n" +
					"                                                            </table>\n" +
					"\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"                                        </div>\n" +
					"                                    </div>\n" +
					"                                </div>\n" +
					"                              </div>\n" +
					"                        </div>\n" +
					"                    </div>\n" +
					"                    <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
					"                        <div class=\"u-row\"\n" +
					"                            style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #f9f9f9;\">\n" +
					"                            <div\n" +
					"                                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
					"                            <div class=\"u-col u-col-100\"\n" +
					"                                    style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
					"                                    <div style=\"height: 100%;width: 100% !important;\">\n" +
					"                                        <!--[if (!mso)&(!IE)]><!-->\n" +
					"                                        <div\n" +
					"                                            style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
					"                                            <!--<![endif]-->\n" +
					"\n" +
					"                                            <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\"\n" +
					"                                                cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
					"                                                <tbody>\n" +
					"                                                    <tr>\n" +
					"                                                        <td style=\"overflow-wrap:break-word;word-break:break-word;padding:0px 40px 30px 20px;font-family:'Lato',sans-serif;\"\n" +
					"                                                            align=\"left\">\n" +
					"\n" +
					"                                                            <div\n" +
					"                                                                style=\"line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
					"\n" +
					"                                                            </div>\n" +
					"\n" +
					"                                                        </td>\n" +
					"                                                    </tr>\n" +
					"                                                </tbody>\n" +
					"                                            </table>\n" +
					"                                        </div>\n" +
					"                                    </div>\n" +
					"                                </div>\n" +
					"                            </div>\n" +
					"                        </div>\n" +
					"                    </div>\n" +
					"                </td>\n" +
					"            </tr>\n" +
					"        </tbody>\n" +
					"    </table>\n" +
					"</body>\n" +
					"\n" +
					"</html>");
		}else{
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
