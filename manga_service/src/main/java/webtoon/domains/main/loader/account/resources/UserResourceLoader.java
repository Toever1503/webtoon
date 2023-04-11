package webtoon.domains.main.loader.account.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webtoon.account.controller.admin.UserResource;

@RestController
@RequestMapping("api/users")
public class UserResourceLoader extends UserResource {
}
