package webtoon.account.controller;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import webtoon.utils.exception.HandleException;

@RestControllerAdvice
public class ExceptionHandler extends HandleException {
}
