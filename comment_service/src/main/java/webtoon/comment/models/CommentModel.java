package webtoon.comment.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentModel {
    private Long id;
    private String content;
    private String commentType;
}
