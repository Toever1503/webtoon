package webtoon.account.configs.security.oauth2;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Builder
public class CustomerOAuth2User implements OAuth2User {

    private OAuth2User oAuth2User;

    @Override
    public Map<String, Object> getAttributes() {
        return this.oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return this.oAuth2User.getName();
    }

    public String getFullName() {
        return this.oAuth2User.getAttribute("name");
    }
}
