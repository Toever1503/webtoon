package webtoon.utils.exception;

import lombok.Data;
import org.springframework.security.core.AuthenticationException;

@Data
public class CustomHandleException extends AuthenticationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code = 0;

	public CustomHandleException(int code) {
		super(null);
		this.code = code;
	}
}
