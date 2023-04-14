package webtoon.comment.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import webtoon.comment.dtos.CommentDto;
import webtoon.comment.entities.CommentEntity;
import webtoon.comment.inputs.CommentInput;
import webtoon.comment.repositories.ICommentRepository;
import webtoon.comment.services.ICommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {

    private final ICommentRepository commentRepository;

    @Override
    public CommentDto findById(Long id) {
        return CommentDto.toDto(
                this.getById(id)
        );
    }

    @Override
    public CommentDto add(CommentInput model) {
        return CommentDto.toDto(
                this.commentRepository.saveAndFlush(
                        CommentEntity.builder()
                                .content(model.getContent())
                                .commentType(model.getCommentType())
                                .objectId(model.getObjectId())
                                .parentComment(model.getParentId() != null ? this.getById(model.getParentId()) : null)
                                .build()
                )
        );
    }

    @Override
    public CommentDto update(CommentInput model) {
        CommentEntity entity = this.getById(model.getId());
        entity.setContent(model.getContent());
        return CommentDto.toDto(
                this.commentRepository.saveAndFlush(entity)
        );
    }

    @Override
    public Page<CommentDto> findAll(Pageable pageable, Specification<CommentEntity> spec) {
        return this.commentRepository.findAll(spec, pageable)
                .map(entity ->
                        CommentDto.builder()
                                .id(entity.getId())
                                .content(entity.getContent())
                                .commentType(entity.getCommentType())
                                .createdAt(entity.getCreatedAt())
                                .modifiedAt(entity.getModifiedAt())
                                .commentParent(entity.getParentComment() != null ? entity.getId() : null)
                                .childComments(commentRepository.findAllByParentId(entity.getId()))
                                .build()
                );
    }

    @Override
    public void delete(Long id) {
        List<CommentEntity> childComments = commentRepository.findAllByParentId(id);
        childComments.forEach(commentEntity -> this.commentRepository.deleteById(commentEntity.getId()));
        this.commentRepository.deleteById(id);
    }

    private CommentEntity getById(Long id) {
        return this.commentRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("0")
                );
    }

}
