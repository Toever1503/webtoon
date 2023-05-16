package webtoon.main.domains.post.service;

import webtoon.main.domains.post.entities.PostEntity;
import webtoon.main.domains.post.entities.dtos.PostDto;
import webtoon.main.domains.post.input.PostInput;

import java.util.List;

public interface IPostService {
    PostDto savePost(PostInput postInput);

    PostDto findByID(Long postID);
    
    PostEntity getByID(Long ID);

    PostDto[] findNextPrevPosts(Long postID, Long categoryId);

    List<PostEntity> findAllPost();
}
