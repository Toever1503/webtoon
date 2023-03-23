package webtoon.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import webtoon.account.models.CreateUserModel;
import webtoon.account.services.impl.UserServiceImpl;

@Controller
@RequestMapping(value = "user/update")
@RequiredArgsConstructor
public class UpdateUserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping(value = "{id}")
    public ModelAndView index(@PathVariable(name = "id") Long id) {
        return new ModelAndView("")
                .addObject("user", this.userServiceImpl.findById(id));
    }

    @PostMapping
    public ModelAndView update(@RequestBody CreateUserModel model, @RequestParam(name = "avatar")MultipartFile file) {
        return new ModelAndView("")
                .addObject("user", this.userServiceImpl.add(model));
    }
}
