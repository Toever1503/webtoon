package webtoon.domains.post.input;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostInput {
	private Long id;

	private String title;

	private String excerpt;

	private String content;

	private String postName;

	private String featuredImage;

	private EPostStatus status;

	private Long category;

	private List<String> tagRelations;
}
