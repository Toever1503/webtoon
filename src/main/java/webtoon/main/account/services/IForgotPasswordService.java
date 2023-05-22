package webtoon.main.account.services;

import org.springframework.stereotype.Service;
import webtoon.main.account.models.ChangePasswordModel;

import javax.mail.MessagingException;

@Service
public interface IForgotPasswordService {


    void sendResetPasswordEmail(String email) throws MessagingException;

    void resetPassword(String token, ChangePasswordModel input);
}
