package webtoon.main.account.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webtoon.main.account.configs.security.jwt.JwtProvider;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.account.services.IForgotPasswordService;
import webtoon.main.account.services.IUserService;
import webtoon.main.payment.services.ISendEmailService;

import javax.mail.MessagingException;
import java.util.Map;

@Service
public class IForgotPasswordServiceImpl implements IForgotPasswordService {
    @Autowired
    private ISendEmailService sendEmailService;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public void sendResetPasswordEmail(String email) throws MessagingException{
        UserEntity entity = this.userService.findByEmail(email);
        String token = this.jwtProvider.generateToken(entity.getUsername(),3600);
        String resetPasswordUrl = "http://localhost:8080/reset-password?token=" + token;
        String text = "Please click the following link to reset your password: " + resetPasswordUrl;
        this.sendEmailService.sendMail("account/forgot_password.html",entity.getEmail(),"Đặt lại mật khẩu của bạn",
                Map.of(text,entity)
                );
    }

    @Override
    public void resetPassword(String userName, String newPassword){
        UserEntity user = userService.findByUserName(userName);
        if (user != null){
            user.setPassword(newPassword);
            userService.saveUserEntity(user);
        }
    }

}
