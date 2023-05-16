package webtoon.account.configs.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import webtoon.account.entities.UserEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CustomUserDetail implements UserDetails {
    private UserEntity user;

    public CustomUserDetail(UserEntity user) {
        this.user = user;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> auths = new ArrayList<>();
        if (user.getAuthorities() != null)
            auths.addAll(user.getAuthorities().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthorityName().name())).collect(Collectors.toList()));

        if (user.getRole() != null)
            auths.add(new SimpleGrantedAuthority(user.getRole().getRoleName().name()));
        return auths;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
