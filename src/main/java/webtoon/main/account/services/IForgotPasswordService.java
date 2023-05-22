package webtoon.main.account.services;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface IForgotPasswordService {


    void sendResetPasswordEmail(String email) throws MessagingException;

    void resetPassword(String userName, String newPassword);
}
