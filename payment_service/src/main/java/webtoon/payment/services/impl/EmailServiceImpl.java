package webtoon.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.entities.UserEntity;
import webtoon.payment.services.ISendEmail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements ISendEmail {
    @Autowired
    private  JavaMailSender mailSender;


    @Override
    public void sendingPayment(String email) {
        String subject = "Payment VNPAY";
        String toEmail = email;
        String content = "Hello ";
        sendEmail(toEmail, subject, content);
    }
    private void sendEmail(final String email, final String subject, final String content){
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, "utf-8");
        try{
            helper.setSubject(subject);
            helper.setTo(email);
            helper.setText(content, true);
            mailSender.send(mime);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
