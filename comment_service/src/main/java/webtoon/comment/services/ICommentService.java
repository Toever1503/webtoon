package webtoon.comment.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import webtoon.comment.dtos.CommentDto;
import webtoon.comment.entities.CommentEntity;
import webtoon.comment.inputs.CommentInput;

public interface ICommentService {
    CommentDto findById(Long id);

    CommentDto add(CommentInput model);

    CommentDto update(CommentInput model);

    Page<CommentDto> findAll(Pageable pageable, Specification<CommentEntity> spec);

    void delete(Long id);
}
