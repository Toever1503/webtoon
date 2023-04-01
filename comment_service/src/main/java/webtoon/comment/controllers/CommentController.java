package webtoon.comment.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.comment.models.CommentModel;
import webtoon.comment.services.ICommentService;

@Controller
@RequestMapping(value = "comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    @GetMapping(value = "get")
    public String get(
            Model model,
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "page", defaultValue = "0") Integer page
    ) {
        model.addAttribute(
                "comment",
                new CommentModel()
        );

        model.addAttribute(
                "commentPage",
                this.commentService.findAllById(
                        id,
                        Pageable.ofSize(10)
                                .withPage(page)
                )
        );
        return "comment/comment-item";
    }

    @PostMapping(value = "create")
    public String create(@ModelAttribute CommentModel commentModel) {
        this.commentService.add(commentModel);

        return "redirect:/comment/get";
    }

    @PostMapping(value = "edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute CommentModel commentModel) {
        commentModel.setId(id);
        this.commentService.update(commentModel);

        return "redirect:/comment/get";
    }

    @GetMapping(value = "delete/{id}")
    public String delete(@PathVariable(name = "id") Long id) {
        this.commentService.delete(id);

        return "redirect:/comment/get";
    }

}
