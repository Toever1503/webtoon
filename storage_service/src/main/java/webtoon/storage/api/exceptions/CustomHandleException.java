package webtoon.storage.api.exceptions;

import lombok.Data;
//import org.springframework.security.core.AuthenticationException;

@Data
public class CustomHandleException extends Exception
//extends AuthenticationException
{
	private int code = 0;

	public CustomHandleException(int code) {
//        super(null);
		this.code = code;
	}
}
