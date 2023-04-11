package webtoon.domains.post.service;

import webtoon.domains.post.entities.PostEntity;
import webtoon.domains.post.entities.dtos.PostDto;
import webtoon.domains.post.input.PostInput;

import java.util.List;

public interface IPostService {
    PostDto savePost(PostInput postInput);

    PostDto findByID(Long postID);
    
    PostEntity getByID(Long ID);

    PostDto[] findNextPrevPosts(Long postID, Long categoryId);

    List<PostEntity> findAllPost();
}
