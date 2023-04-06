package webtoon.comment.inputs;

import lombok.*;
import webtoon.comment.enums.ECommentType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentInput {
    private Long id;
    private String content;
    private ECommentType commentType;

    private Long parentId;
    private Long objectId;
}
