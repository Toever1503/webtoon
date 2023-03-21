package webtoon.domains.post.service;

import webtoon.domains.post.entities.PostEntity;
import webtoon.domains.post.entities.dtos.PostDto;
import webtoon.domains.post.input.PostInput;

public interface IPostService {
    PostDto savePost(PostInput postInput);

    PostDto findByID(Long postID);
    
    PostEntity getByID(Long ID);

    PostDto[] findNextPrevPosts(Long postID, Long categoryId);
}
