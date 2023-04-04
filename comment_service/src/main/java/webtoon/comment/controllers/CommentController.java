package webtoon.comment.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.comment.enums.ECommentType;
import webtoon.comment.inputs.CommentInput;
import webtoon.comment.services.ICommentService;

@Controller
@RequestMapping(value = "comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    @GetMapping
    public String listComment(Model model,
                              @RequestParam(name = "objectId") Long id,
                              @RequestParam(name = "commentType") ECommentType commentType,
                              Pageable page) {
        model.addAttribute("commentPage", null );

        return "comment/comment-item";
    }

    @PostMapping(value = "create")
    public String create(@ModelAttribute CommentInput commentModel) {
        this.commentService.add(commentModel);

        return "redirect:/comment/get";
    }

    @PostMapping(value = "edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute CommentInput commentModel) {
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
