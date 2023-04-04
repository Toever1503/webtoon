package webtoon.comment.inputs;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentInput {
    private Long id;
    private String content;
    private String commentType;
}
