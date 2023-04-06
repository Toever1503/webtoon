package webtoon.comment.dtos;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import webtoon.comment.entities.CommentEntity;
import webtoon.comment.enums.ECommentType;
import webtoon.comment.repositories.ICommentRepository;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class CommentDto {

    private final Long id;

    private final String content;

    private final ECommentType commentType;

    private final Date createdAt;

    private final Date modifiedAt;

    private final Long createdBy;

    private final Long modifiedBy;

    private Long commentParent;
    private List<CommentEntity> childComments;

    public static CommentDto toDto(CommentEntity entity) {
        return CommentDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .commentType(entity.getCommentType())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .commentParent(entity.getParentComment() != null ? entity.getId() : null)
                .build();
    }
}
