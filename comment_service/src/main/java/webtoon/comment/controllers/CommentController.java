package webtoon.comment.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webtoon.comment.entities.CommentEntity_;
import webtoon.comment.services.ICommentService;
import webtoon.comment.entities.CommentEntity;
import webtoon.comment.enums.ECommentType;
import webtoon.comment.inputs.CommentInput;

@Controller
@RequestMapping(value = "comments")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    @RequestMapping
    public String listComment(Model model, @RequestParam(name = "objectId") Long objectId, @RequestParam(name = "commentType") ECommentType commentType, Pageable page) {

        Specification specs = Specification.where((root, query, builder) ->
                builder.equal(root.get(CommentEntity_.OBJECT_ID), objectId))
                .and((root, query, builder) -> builder.equal(root.get(CommentEntity_.COMMENT_TYPE), commentType))
                .and((root, query, builder) -> builder.isNull(root.get(CommentEntity_.PARENT_COMMENT)));
        Page<CommentEntity> commentEntityPage = this.commentService.findAll(page, specs);
        model.addAttribute("commentPage", commentEntityPage);
        model.addAttribute("commentInput", new CommentInput());

        model.addAttribute("objectId", objectId);
        model.addAttribute("commentType", commentType);

        model.addAttribute("hasPrevPage", commentEntityPage.hasPrevious());
        model.addAttribute("hasNextPage", commentEntityPage.hasNext());
        model.addAttribute("currentPage", commentEntityPage.getNumber());
        model.addAttribute("totalPage", commentEntityPage.getTotalPages());
        return "comment/index";
    }

    @PostMapping(value = "create")
    public String create(@ModelAttribute CommentInput commentModel, @RequestParam(name = "objectId") Long objectId, @RequestParam(name = "commentType") ECommentType commentType) {
        commentModel.setCommentType(commentType);
        commentModel.setObjectId(objectId);
        this.commentService.add(commentModel);
        return "redirect:/comments?objectId=" + objectId + "&commentType=" + commentType;
    }

    @PostMapping(value = "update/{id}")
    public String update(@PathVariable(name = "id") Long id, @ModelAttribute CommentInput commentModel, @RequestParam(name = "objectId") Long objectId, @RequestParam(name = "commentType") ECommentType commentType) {
        commentModel.setId(id);
        commentModel.setCommentType(commentType);
        commentModel.setObjectId(objectId);
        this.commentService.update(commentModel);

        return "redirect:/comments?objectId=" + objectId + "&commentType=" + commentType;
    }

    @GetMapping(value = "delete/{id}")
    public String delete(@PathVariable(name = "id") Long id, @ModelAttribute CommentInput commentModel, @RequestParam(name = "objectId") Long objectId, @RequestParam(name = "commentType") ECommentType commentType) {
        this.commentService.delete(id);
        return "redirect:/comments?objectId=" + objectId + "&commentType=" + commentType;
    }

}
