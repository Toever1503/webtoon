package webtoon.comment.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import webtoon.comment.dtos.CommentDto;
import webtoon.comment.models.CommentModel;

public interface ICommentService {
    CommentDto findById(Long id);

    Page<CommentDto> findAllById(Long id, Pageable pageable);

    CommentDto add(CommentModel model);

    CommentDto update(CommentModel model);

    void delete(Long id);
}
