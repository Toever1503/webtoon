package webtoon.payment.services;

import javax.mail.MessagingException;

public interface ISendEmail {
    void sendingPayment(String email) throws MessagingException;
    void sendEmail(String email, String subject, String body) throws MessagingException;
}
