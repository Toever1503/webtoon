package webtoon.main.account.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import webtoon.main.account.configs.security.jwt.JwtProvider;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.account.models.ChangePasswordModel;
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
    @Lazy
    private IUserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void sendResetPasswordEmail(String email) throws MessagingException {
        try {
            UserEntity entity = this.userService.findByEmail(email);
            String token = this.jwtProvider.generateToken(entity.getUsername(), 86400);
            this.sendEmailService.sendMail("account/mail-templates/reset_password.html", entity.getEmail(), "Đặt lại mật khẩu của bạn",
                    Map.of("token", token)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resetPassword(String token, ChangePasswordModel input) {
        String username = this.jwtProvider.getUsernameFromToken(token);
        UserEntity entity = this.userService.findByUserName(username);
        entity.setPassword(this.passwordEncoder.encode(input.getNewPassword().trim()));
        this.userService.saveUserEntity(entity);
    }


}
