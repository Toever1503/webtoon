package webtoon.account.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import webtoon.account.dtos.UserDto;
import webtoon.account.entities.AuthorityEntity;
import webtoon.account.entities.UserEntity;
import webtoon.account.entities.UserEntity_;
import webtoon.account.enums.ESex;
import webtoon.account.enums.EStatus;
import webtoon.account.inputs.UserFilterInput;
import webtoon.account.inputs.UserInput;
import webtoon.account.services.IUserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserResource {

    @Autowired
    private IUserService userService;

    @PostMapping("filter")
    public Page<UserDto> filterUser(@RequestBody UserFilterInput input, Pageable pageable) {

        List<Specification> specs = new ArrayList<>();
        if (input.getQ() != null)
        {
            String qLike = "%" + input.getQ() + "%";
            specs.add((root, query, cb) ->
                    cb.or(
                            cb.like(root.get(UserEntity_.FULL_NAME), qLike),
                            cb.like(root.get(UserEntity_.USERNAME), qLike),
                            cb.like(root.get(UserEntity_.EMAIL), qLike),
                            cb.like(root.get(UserEntity_.PHONE), qLike)
//                            cb.like(root.get(UserEntity_.sex), Arrays.stream(ESex.values()).anyMatch(e -> e.name().equalsIgnoreCase(qLike)) ? qLike : "")
                    )
            );
        }
        Specification<UserEntity> finalSpec = null;
        for (Specification<UserEntity> spec : specs) {
            if (finalSpec == null)
                finalSpec = spec;
            else
                finalSpec = finalSpec.and(spec);
        }
        return this.userService.findAll(pageable, finalSpec);
    }

    @GetMapping("all-authorities")
    public List<AuthorityEntity> getAllAuthorities() {
        return userService.getAllAuthorities();
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserInput input) {
        return this.userService.add(input);
    }

    @PutMapping("{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserInput input) {
        input.setId(id);
        return this.userService.update(input);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Long id) {
        this.userService.delete(id);
    }

    @PatchMapping("{id}/change-status")
    public void changeStatus(@PathVariable Long id, @RequestParam EStatus status) {
        this.userService.changeStatus(id, status);
    }

    @PatchMapping("{id}/reset-block")
    public void unBlockUser(@PathVariable Long id) {
        this.userService.unHasBlockedByNumberOfFailedSignInAndResetNumberOfFailedSignIn(id);
    }
}
