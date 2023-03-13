package webtoon.domains.post.input;

import java.util.List;

import javax.validation.constraints.NotNull;

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

	@NotNull
	private String title;

	@NotNull
	private String excerpt;

	@NotNull
	private String content;

	@NotNull
	private String postName;

	@NotNull
	private String featuredImage;

	@NotNull
	private EPostStatus status;

	@NotNull
	private Long category;

	@NotNull
	private List<String> tagRelations;
}
