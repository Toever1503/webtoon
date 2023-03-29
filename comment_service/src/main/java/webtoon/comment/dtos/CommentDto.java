package webtoon.comment.dtos;

import lombok.Builder;
import lombok.Getter;
import webtoon.comment.dtos.UserDto;
import webtoon.comment.entities.CommentEntity;
import webtoon.comment.enums.ECommentType;

import java.util.Date;

@Getter
@Builder
public class CommentDto {
    private final Long id;

    private final String content;

    private final ECommentType commentType;

    private final Date createdAt;

    private final Date modifiedAt;

    private final UserDto createdBy;

    private final UserDto modifiedBy;

    public static CommentDto toDto(CommentEntity entity) {
        return CommentDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .commentType(entity.getCommentType())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .createdBy(
                        UserDto.toDto(entity.getCreatedBy())
                )
                .modifiedBy(
                        UserDto.toDto(entity.getModifiedBy())
                )
                .build();
    }
}
