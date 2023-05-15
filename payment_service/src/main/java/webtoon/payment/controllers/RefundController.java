package webtoon.payment.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webtoon.payment.services.IOrderService;
import webtoon.payment.services.IPaymentService;
import webtoon.payment.services.ISendEmailService;
import webtoon.payment.services.ISubscriptionPackService;

@Controller
@RequestMapping("payment/refund")
public class RefundController {

    @Autowired
    private ISendEmailService sendEmail;
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


}
