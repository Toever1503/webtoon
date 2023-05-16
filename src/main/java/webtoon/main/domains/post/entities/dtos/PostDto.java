package webtoon.main.domains.post.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webtoon.main.domains.post.input.EPostStatus;
import webtoon.main.domains.tag.entity.TagEntity;
import webtoon.main.domains.post.entities.CategoryEntity;

import java.util.Date;
import java.util.List;

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

}