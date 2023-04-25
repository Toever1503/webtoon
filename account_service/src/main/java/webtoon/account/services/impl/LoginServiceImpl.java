package webtoon.account.services.impl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webtoon.account.configs.security.CustomUserDetail;
import webtoon.account.configs.security.jwt.JwtProvider;
import webtoon.account.entities.EAuthorityConstants;
import webtoon.account.entities.UserEntity;
import webtoon.account.enums.EAccountType;
import webtoon.account.enums.EStatus;
import webtoon.account.models.LoginModel;
import webtoon.account.repositories.IAuthorityRepository;
import webtoon.account.repositories.IUserRepository;
import webtoon.account.services.ILoginService;
import webtoon.utils.exception.CustomHandleException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;


@Service
@Transactional
public class LoginServiceImpl implements ILoginService {

    private final PasswordEncoder passwordEncoder;

    private final IUserRepository userRepository;

    private final IAuthorityRepository authorityRepository;

    private final JwtProvider jwtProvider;

    public LoginServiceImpl(PasswordEncoder passwordEncoder, IUserRepository userRepository, IAuthorityRepository authorityRepository, JwtProvider jwtProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.jwtProvider = jwtProvider;
    }


    @Override
    public void login(LoginModel model, HttpServletRequest req) {
        UserEntity userEntity = this.userRepository
                .findByUsernameOrEmail(model.getUsername(), model.getUsername())
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );

        if (!this.passwordEncoder.matches(model.getPassword(), userEntity.getPassword())) {
            throw new CustomHandleException(1);
        }

        req.getSession().setAttribute("loggedUser", userEntity);
        CustomUserDetail userDetail = new CustomUserDetail(userEntity);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }


    @Override
    public int loginViaOAuth2(OAuth2AuthenticationToken token, HttpServletRequest req, HttpServletResponse res) {
        EAccountType accountType = EAccountType.valueOf(token.getAuthorizedClientRegistrationId().toUpperCase());
        OAuth2User oAuth2User = token.getPrincipal();

        String username;
        String email = oAuth2User.getAttribute("email");
        if (accountType == EAccountType.GOOGLE)
            username = accountType + "-" + oAuth2User.getAttribute("sub");
        else username = accountType + "-" + oAuth2User.getAttribute("id");

        UserEntity entity = this.userRepository.findByUsernameOrEmail(username, email)
                .orElse(null);

        int typeOfLogin = 0;
        if (entity == null) { // handle new user
            entity = UserEntity.builder()
                    .username(username)
                    .accountType(accountType)
                    .email(email)
                    .fullName(oAuth2User.getAttribute("name"))
                    .hasBlocked(false)
                    .numberOfFailedSignIn(1)
                    .status(EStatus.ACTIVED)
                    .password(this.passwordEncoder.encode(username))
                    .authorities(Set.of(this.authorityRepository.findByAuthorityName(EAuthorityConstants.ROLE_USER)))
                    .build();
            if (accountType == EAccountType.GOOGLE)
                entity.setAvatar(oAuth2User.getAttribute("picture"));
            this.userRepository.saveAndFlush(entity);
            typeOfLogin = 1;
        } else { // handle old user

        }

        req.getSession().setAttribute("loggedUser", entity);
        Cookie tokenCookie = new Cookie("token", jwtProvider.generateToken(entity.getUsername(), 86400));
        tokenCookie.setMaxAge(86400);
        res.addCookie(tokenCookie);

        CustomUserDetail userDetail = new CustomUserDetail(entity);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        return typeOfLogin;
    }


}
