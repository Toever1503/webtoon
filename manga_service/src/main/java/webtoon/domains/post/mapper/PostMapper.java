package webtoon.domains.post.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import webtoon.domains.post.entities.ICategoryRepository;
import webtoon.domains.post.entities.IPostRepository;
import webtoon.domains.post.entities.PostEntity;
import webtoon.domains.post.entities.dtos.PostDto;
import webtoon.domains.post.input.PostInput;
import webtoon.domains.tag.entity.ITagRelationRepository;
import webtoon.domains.tag.entity.ITagRepository;
import webtoon.domains.tag.entity.TagEntity;
import webtoon.domains.tag.entity.TagEntityRelation;

@Component
public class PostMapper {

	private final ICategoryRepository categoryRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public PostMapper( ICategoryRepository categoryRepository) {
		super();
		this.categoryRepository = categoryRepository;
	}

	public PostDto toDto(PostEntity entity) {
		this.logger.info("PostMapper`s converting entity id: {} to dto.", entity.getId());
		return PostDto.builder().id(entity.getId()).title(entity.getTitle()).excerpt(entity.getExcerpt())
				.content(entity.getContent()).commentCount(entity.getCommentCount()).viewCount(entity.getViewCount())
				.status(entity.getStatus()).createdBy(entity.getCreatedBy()).modifiedBy(entity.getModifiedBy())
				.createdDate(entity.getCreatedDate()).modifiedDate(entity.getModifiedDate())
				.featuredImage(entity.getFeaturedImage()).category(entity.getCategory())
				.tags(entity.getTagRelations() != null
						? entity.getTagRelations().stream().map(t -> t.getTag()).collect(Collectors.toList())
						: Collections.emptyList())
				.build();
	}

	public PostEntity toEntity(PostInput input) {
		this.logger.info("PostMapper`s converting input.");

		PostEntity entity = PostEntity.builder().id(input.getId()).title(input.getTitle()).excerpt(input.getExcerpt())
				.content(input.getContent()).status(input.getStatus()).createdBy(1L).modifiedBy(1L)
				.featuredImage(input.getFeaturedImage()).build();
		entity.setCategory(this.categoryRepository.getById(input.getCategory()));


		return entity;
	}

	public List<PostDto> toDtoList(List<PostEntity> entities) {
		return entities.stream().map(this::toDto).collect(Collectors.toList());
	}

	public Page<PostDto> toDtoPage(Page<PostEntity> entityPage) {
		return entityPage.map(this::toDto);
	}

}
