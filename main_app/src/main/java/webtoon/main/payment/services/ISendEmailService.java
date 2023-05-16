package webtoon.main.payment.services;

import javax.mail.MessagingException;
import java.util.Map;

public interface ISendEmailService {
    void sendingPayment(String email) throws MessagingException;
    void sendEmail(String email, String subject, String body) throws MessagingException;

    void sendMail(String templatePath, String to, String subject, Map<String, Object> content) throws MessagingException;

}
