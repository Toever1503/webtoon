package webtoon.main.utils.infra.security.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IJwtService {
	boolean tokenFilter( String token, HttpServletRequest req, HttpServletResponse res);
}
