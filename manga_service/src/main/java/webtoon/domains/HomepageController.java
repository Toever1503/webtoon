package webtoon.domains;


import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@Controller
public class HomepageController {

//	private final Logger logger = LoggerFactory
    public HomepageController() {
		super();
		System.out.println("homepage controller created!");
		// TODO Auto-generated constructor stub
	}

	@RequestMapping("/")
    public String homepage(){
        return "homepage";
    }
}
