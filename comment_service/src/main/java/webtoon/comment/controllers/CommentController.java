package webtoon.comment.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.comment.entities.CommentEntity;
import webtoon.comment.entities.CommentEntity_;
import webtoon.comment.enums.ECommentType;
import webtoon.comment.inputs.CommentInput;
import webtoon.comment.services.ICommentService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    @RequestMapping
    public String listComment(Model model, @RequestParam(name = "objectId") Long objectId, @RequestParam(name = "commentType") ECommentType commentType, Pageable page) {

        Specification specs = Specification.where((root, query, builder) -> builder.equal(root.get(CommentEntity_.OBJECT_ID), objectId)).and((root, query, builder) -> builder.equal(root.get(CommentEntity_.COMMENT_TYPE), commentType));
        Page<CommentEntity> commentEntityPage = this.commentService.findAll(page, specs);
        model.addAttribute("commentPage", commentEntityPage);
        model.addAttribute("commentInput", new CommentInput());
        return "index";
    }

    @PostMapping(value = "create")
    public String create(@ModelAttribute CommentInput commentModel) {
        this.commentService.add(commentModel);

        return "redirect:/comment?objectId=1&commentType=MANGA";
    }

    @PostMapping(value = "update/{id}")
    public String update(@PathVariable(name = "id") Long id, @ModelAttribute CommentInput commentModel) {
        commentModel.setId(id);
        this.commentService.update(commentModel);

        return "redirect:/comment?objectId=1&commentType=MANGA";
    }

    @GetMapping(value = "delete/{id}")
    public String delete(@PathVariable(name = "id") Long id) {
        this.commentService.delete(id);

        return "redirect:/comment?objectId=1&commentType=MANGA";
    }

}
