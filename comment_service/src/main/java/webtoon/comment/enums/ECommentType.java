package webtoon.comment.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ECommentType {
    MANGA("Manga"),
    POST("Post");

    private final String value;
}
