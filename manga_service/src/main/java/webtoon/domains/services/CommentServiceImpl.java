package webtoon.domains.services;

import org.springframework.stereotype.Service;
import webtoon.comment.repositories.ICommentRepository;

@Service
public class CommentServiceImpl extends webtoon.comment.services.impl.CommentServiceImpl {
    public CommentServiceImpl(ICommentRepository commentRepository) {
        super(commentRepository);
    }
}
