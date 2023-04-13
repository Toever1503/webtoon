package webtoon.main.utils.infra.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetail implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5564487111247690516L;
	private final User principle;
	private List<String> authorities;

	public CustomUserDetail(User principle, List<String> authorities) {
		this.principle = principle;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	public Object getPrinciple() {
		return this.principle;
	}

	@Override
	public String getPassword() {
		return this.principle.getPassword();
	}

	@Override
	public String getUsername() {
		return this.principle.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.principle.isEnabled();
	}
}
