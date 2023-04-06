package webtoon.domains.services;

import org.springframework.stereotype.Service;
import webtoon.comment.repositories.ICommentRepository;

@Service
public class CommentServiceLoader extends webtoon.comment.services.impl.CommentServiceImpl {
    public CommentServiceLoader(ICommentRepository commentRepository) {
        super(commentRepository);
    }
}
