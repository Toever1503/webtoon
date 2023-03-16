package webtoon.domains.post.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import webtoon.domains.post.entities.IPostRepository;
import webtoon.domains.post.entities.PostEntity;
import webtoon.domains.post.entities.dtos.PostDto;
import webtoon.domains.post.input.PostInput;
import webtoon.domains.post.mapper.PostMapper;
import webtoon.domains.tag.entity.ITagRelationRepository;
import webtoon.domains.tag.entity.ITagRepository;
import webtoon.domains.tag.entity.TagEntity;
import webtoon.domains.tag.entity.TagEntityRelation;
import webtoon.domains.tag.entity.enums.TagRelationTypeConstant;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements IPostService {
    private final IPostRepository postRepository;
    private final ITagRepository tagRepository;
    private final ITagRelationRepository tagRelationRepository;
    private final PostMapper postMapper;

    public PostServiceImpl(IPostRepository postRepository, ITagRepository tagRepository,
                           ITagRelationRepository tagRelationRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.tagRelationRepository = tagRelationRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostDto savePost(PostInput input) {
        PostEntity entity = this.postMapper.toEntity(input);
        this.postRepository.saveAndFlush(entity);

        if (input.getTagRelations() != null) {
            List<TagEntityRelation> tags = new ArrayList<>();

            input.getTagRelations().forEach(t -> {
                TagEntity tag = this.tagRepository.findByTagName(t).orElse(TagEntity.builder().tagName(t).build());
                TagEntityRelation relation = this.tagRelationRepository.findByObjectIDAndTagType(entity.getId(), TagRelationTypeConstant.POST);
                if (relation != null) {
                    tags.add(relation);
                } else
                    tags.add(TagEntityRelation.builder().objectID(entity.getId()).tag(tag).tagType(TagRelationTypeConstant.POST).build());
            });

            this.tagRelationRepository.saveAllAndFlush(tags);
            entity.setTagRelations(tags);
        }

        return this.postMapper.toDto(entity);
    }

    @Override
    public PostDto findByID(Long postID) {
        // TODO Auto-generated method stub
        return this.postMapper.toDto(this.getByID(postID));
    }

    @Override
    public PostEntity getByID(Long ID) {
        // TODO Auto-generated method stub
        return this.postRepository.findById(ID).orElseThrow(() -> new RuntimeException("dasdas"));
    }

    @Override
    public PostDto[] findNextPrevPosts(Long postID, Long categoryId) {
        PostDto[] postDtos = new PostDto[2];

        List<PostEntity> nextPosts = this.postRepository.findNextPost(postID, categoryId, PageRequest.of(0, 1));
        postDtos[1] = nextPosts.size() == 0 ? null : this.postMapper.toDto(nextPosts.get(0));

        List<PostEntity> prevPosts = this.postRepository.findPrevPost(postID, categoryId, PageRequest.of(0, 1, Sort.Direction.DESC, "id"));
        postDtos[0] = prevPosts.size() == 0 ? null : this.postMapper.toDto(prevPosts.get(0));

        return postDtos;
    }
}
