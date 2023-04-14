package webtoon.account.services;

import webtoon.account.entities.EmailDetails;

public interface IEmailService {
    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
