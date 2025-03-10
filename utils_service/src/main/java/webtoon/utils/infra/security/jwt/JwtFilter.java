package webtoon.utils.infra.security.jwt;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import webtoon.utils.exception.ResponseDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {
	private final Logger log = LoggerFactory.getLogger(JwtFilter.class);
	private final IJwtService jwtService; // need change to call api

	public JwtFilter(IJwtService userService) {
		this.jwtService = userService;
	}

	// Filter jwt token
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("JwtFilter is checking");
		// If request method is options, do filter
		if (req.getMethod().equalsIgnoreCase("OPTIONS") || req.getServletPath().equals("/socket")) {
			filterChain.doFilter(req, res);
		}
		// if not, checking token and do filter afterward
		else {
			String token = req.getHeader("Authorization");
			System.out.println("token: " + token);
			if (token == null) {
				// token null
				res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				res.getWriter().println(new JSONObject(ResponseDto.ofError(3)));
			} else {
				if (this.jwtService.tokenFilter(token.substring(7), req, res))
					filterChain.doFilter(req, res);
				else {
					// token not valid
					res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					res.getWriter().println(new JSONObject(ResponseDto.ofError(3)));
				}
			}
		}

	}
}
