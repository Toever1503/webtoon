package webtoon.domains.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import webtoon.comment.services.ICommentService;

@Controller
@RequestMapping(value = "comments")
public class CommentControllerImpl extends webtoon.comment.controllers.CommentController {
    public CommentControllerImpl(ICommentService commentService) {
        super(commentService);
        System.out.println("CommentControllerImpl");

    }
}
