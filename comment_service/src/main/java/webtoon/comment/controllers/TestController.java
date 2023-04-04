package webtoon.comment.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("index.html")
    public String testIndex() {
        System.out.printf("homepage index");
        return "index";
    }
}
