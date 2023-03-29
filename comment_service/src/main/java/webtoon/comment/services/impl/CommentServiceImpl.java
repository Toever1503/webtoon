package webtoon.comment.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import webtoon.comment.dtos.CommentDto;
import webtoon.comment.entities.CommentEntity;
import webtoon.comment.enums.ECommentType;
import webtoon.comment.models.CommentModel;
import webtoon.comment.repositories.ICommentRepository;
import webtoon.comment.services.ICommentService;
import webtoon.utils.exception.CustomHandleException;

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
    public Page<CommentDto> findAllById(Long id, Pageable pageable) {
        return this.commentRepository.findAll(pageable)
                .map(CommentDto::toDto);
    }

    @Override
    public CommentDto add(CommentModel model) {
        return CommentDto.toDto(
                this.commentRepository.saveAndFlush(
                        CommentEntity.builder()
                                .content(model.getContent())
                                .commentType(
                                        ECommentType.valueOf(
                                                model.getCommentType().toUpperCase())
                                )
                                .build()
                )
        );
    }

    @Override
    public CommentDto update(CommentModel model) {
        CommentEntity entity = this.getById(model.getId());
        entity.setContent(model.getContent());
        entity.setCommentType(
                ECommentType.valueOf(
                        model.getCommentType().toUpperCase())
        );

        return CommentDto.toDto(
                this.commentRepository.saveAndFlush(entity)
        );
    }

    @Override
    public void delete(Long id) {
        this.commentRepository.deleteById(id);
    }

    private CommentEntity getById(Long id) {
        return this.commentRepository.findById(id)
                .orElseThrow(
                        () -> new CustomHandleException(0)
                );
    }

}
