package webtoon.comment.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentModel {
    private Long id;
    private String content;
    private String commentType;
}
