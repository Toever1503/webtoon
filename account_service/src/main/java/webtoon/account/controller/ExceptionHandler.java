package webtoon.account.controller;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import webtoon.main.utils.exception.HandleException;

@RestControllerAdvice
public class ExceptionHandler extends HandleException {
}
