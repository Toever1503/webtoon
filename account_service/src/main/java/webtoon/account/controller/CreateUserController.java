package webtoon.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import webtoon.account.models.CreateUserModel;
import webtoon.account.services.impl.UserServiceImpl;

@Controller
@RequestMapping(value = "user/create")
@RequiredArgsConstructor
public class CreateUserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping
    public String index() {
        return "";
    }

    @PostMapping
    public ModelAndView add(@RequestBody CreateUserModel model) {
        return new ModelAndView("")
                .addObject(this.userServiceImpl.add(model));
    }
}
