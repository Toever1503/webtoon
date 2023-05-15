package webtoon.payment.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webtoon.account.services.IUserService;
import webtoon.payment.entities.OrderEntity;
import webtoon.payment.entities.PaymentEntity;
import webtoon.payment.entities.SubscriptionPackEntity;
import webtoon.payment.enums.EOrderStatus;
import webtoon.payment.services.IOrderService;
import webtoon.payment.services.IPaymentService;
import webtoon.payment.services.ISendEmailService;
import webtoon.payment.services.ISubscriptionPackService;

@Controller
@RequestMapping("payment")
public class PaymentController {

    //	@Autowired
//	private ISendEmail sendEmail;
    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ISubscriptionPackService subscriptionPackService;

    @Autowired
    private ISendEmailService sendEmailService;

    @Autowired
    private IUserService userService;

    @RequestMapping("pay")
    public void test(HttpServletResponse resp,
                     @RequestParam Long orderId) throws IOException, ParseException {
        OrderEntity orderEntity = this.orderService.getById(orderId);
        SubscriptionPackEntity subscriptionPackEntity = orderEntity.getSubs_pack_id();

        Integer amount = subscriptionPackEntity.getPrice().intValue();
        String vnp_OrderInfo = "Thanh toan goi " + subscriptionPackEntity.getMonthCount() + " thang.";
        String vnp_TxnRef = orderEntity.getMaDonHang();
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
        cldvnp_ExpireDate.add(Calendar.MINUTE, 15);
        Date vnp_ExpireDateD = cldvnp_ExpireDate.getTime();

        System.out.println("expireDate: " + vnp_ExpireDateD);
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

    @RequestMapping("/ket-qua")
    public String ketQua(HttpServletRequest request,
                         Model model, HttpSession httpSession) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String maDonHang = request.getParameter("vnp_TxnRef");
        String tinhTrangThanhToan = request.getParameter("vnp_TransactionStatus");
        String thoiGianTT = request.getParameter("vnp_PayDate");

        model.addAttribute("isSuccess", "00".equals(tinhTrangThanhToan));
        if ("00".equals(tinhTrangThanhToan)) {

            OrderEntity orderEntity = this.orderService.getMaDonHang(maDonHang);
            orderEntity.setStatus(EOrderStatus.COMPLETED);

            Calendar currentDate = Calendar.getInstance();
            currentDate.add(Calendar.MONTH, orderEntity.getSubs_pack_id().getMonthCount());
            this.orderService.saveOrderEntity(orderEntity);

            PaymentEntity paymentEntity = PaymentEntity.builder()
                    .orderId(orderEntity)
                    .transNo(request.getParameter("vnp_TransactionNo"))
                    .payMoney(orderEntity.getFinalPrice())
                    .bankTranNo(request.getParameter("vnp_TransactionNo"))
                    .bankCode(request.getParameter("vnp_BankCode"))
                    .cardType(request.getParameter("vnp_CardType"))
                    .status(Integer.valueOf(tinhTrangThanhToan))
                    .paidDate(formatter.parse(thoiGianTT))
                    .orderInfo(request.getParameter("vnp_OrderInfo"))
                    .build();
            paymentService.add(paymentEntity);


            this.orderService.plusReadTimeToUser(orderEntity);

            httpSession.setAttribute("loggedUser", orderEntity.getUser_id());

            try {
                this.sendEmailService.sendMail("payments/mail-templates/order_info.html",
                        orderEntity.getUser_id().getEmail(),
                        "Đơn hàng của bạn đã được thanh toán thành công!",
                        Map.of("order", orderEntity));
            } catch (Exception e) {
                System.out.printf("send mail failed!");
                e.printStackTrace();

            }

            model.addAttribute("maDonHang", maDonHang);
            model.addAttribute("soTien", orderEntity.getFinalPrice());
            model.addAttribute("noiDungTT", paymentEntity.getOrderInfo());
            model.addAttribute("maPhanHoi", tinhTrangThanhToan);
            model.addAttribute("maGD", paymentEntity.getTransNo());
            model.addAttribute("maNganHang", paymentEntity.getBankCode());
            model.addAttribute("thoiGianTT", paymentEntity.getPaidDate());
            model.addAttribute("ketQua", "Giao dịch thành công");

        } else {
            model.addAttribute("ketQua", "Giao dịch không thành công");
        }

        return "payments/order_detail";
    }
}
