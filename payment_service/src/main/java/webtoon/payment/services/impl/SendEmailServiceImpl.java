package webtoon.payment.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.entities.UserEntity;
import webtoon.payment.services.ISendEmail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class SendEmailServiceImpl implements ISendEmail {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendingPayment(String email) {
        UserEntity userEntity = SecurityUtils.getCurrentUser().getUser();
        String emailUser = userEntity.getEmail();
        String subject = "Payment Success";
        String body = "Thank you for your payment. Your payment is successful. Your email is " + emailUser;
        sendEmail(email, subject, body);
    }

    public void sendEmail(String email, String subject, String body) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
        try {
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
