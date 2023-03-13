package webtoon.domains.post.service;

import webtoon.domains.post.entities.dtos.PostDto;
import webtoon.domains.post.input.PostInput;

public interface IPostService {
    PostDto savePost(PostInput postInput);

}
