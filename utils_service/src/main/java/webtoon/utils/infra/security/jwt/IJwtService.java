package webtoon.utils.infra.security.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;

public interface IJwtService {
	boolean tokenFilter(@NonNull String token, HttpServletRequest req, HttpServletResponse res);
}
