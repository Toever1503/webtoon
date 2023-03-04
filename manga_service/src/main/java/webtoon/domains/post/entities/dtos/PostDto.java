package webtoon.domains.post.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.domains.post.entities.CategoryEntity;
import webtoon.domains.post.entities.PostEntity;
import webtoon.domains.post.input.EPostStatus;
import webtoon.domains.tag.entity.TagEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
	private Long id;

	private String title;

	private String excerpt;

	private String content;

	private String postName;

	private Integer commentCount;

	private Integer viewCount;

	private EPostStatus status;

	private Long createdBy;

	private Long modifiedBy;

	private Date createdDate;

	private Date modifiedDate;

	private String featuredImage;

	private CategoryEntity category;

	private List<TagEntity> tags;

	public static PostDto toDto(PostEntity postEntity) {
		if (postEntity == null)
			return null;
		return PostDto.builder().id(postEntity.getId()).title(postEntity.getTitle()).excerpt(postEntity.getExcerpt())
				.content(postEntity.getContent()).commentCount(postEntity.getCommentCount())
				.viewCount(postEntity.getViewCount()).status(postEntity.getStatus())
				.createdBy(postEntity.getCreatedBy()).modifiedBy(postEntity.getModifiedBy())
				.createdDate(postEntity.getCreatedDate()).modifiedDate(postEntity.getModifiedDate())
				.featuredImage(postEntity.getFeaturedImage()).category(postEntity.getCategory())
				.tags(postEntity.getTagRelations() != null
						? postEntity.getTagRelations().stream().map(t -> t.getTag()).collect(Collectors.toList())
						: Collections.emptyList())
				.build();
	}
}