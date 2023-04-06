package webtoon.domains.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import webtoon.comment.controllers.CommentController;
import webtoon.comment.services.ICommentService;

@Controller
@RequestMapping(value = "comments")
public class CommentControllerLoader extends CommentController {
    public CommentControllerLoader(ICommentService commentService) {
        super(commentService);
    }
}
