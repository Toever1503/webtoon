package webtoon.main.domains.manga.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MangaVolumeFilter {
	private String name;

	private Integer volumeIndex;
}
