package webtoon.payment.services.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import webtoon.account.configs.security.SecurityUtils;
import webtoon.account.entities.UserEntity;
import webtoon.payment.services.ISendEmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class SendEmailServiceImpl implements ISendEmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final Logger mailLogger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void sendingPayment(String email) {
        UserEntity userEntity = SecurityUtils.getCurrentUser().getUser();
        String emailUser = userEntity.getEmail();
        String subject = "Payment Success";
        String body = "Thank you for your payment. Your payment is successful. Your email is " + emailUser;
        sendEmail(email, subject, body);
    }
    @Override
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

    @Override
    public void sendMail(String templatePath, String to, String subject, Map<String, Object> content) throws MessagingException {
        mailLogger.info("Begin send mail");
        MimeMessage message = javaMailSender.createMimeMessage(); // init new SimpleMailMessage
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        //add variable
        Context context = new Context();
        context.setVariables(content);

        String contentHtml = templateEngine.process(templatePath, context);

        // set recipient, subject, content
        messageHelper.setFrom("noreply@animenews.life");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(contentHtml, true);

        javaMailSender.send(message); // send mail
        mailLogger.info("Send mail to ".concat(to.concat(" successfully!")));
    }
}
