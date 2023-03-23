package webtoon.domains.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PostResource {

	@RequestMapping("/")
	public ModelAndView homepage() {
		System.out.println("hello 11");
		ModelAndView modelAndView = new ModelAndView("homepage");
		return modelAndView;
	}
}
