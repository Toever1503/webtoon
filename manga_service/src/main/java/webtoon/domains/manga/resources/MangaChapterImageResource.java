package webtoon.domains.manga.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import webtoon.domains.manga.dtos.MangaChapterImageDto;
import webtoon.domains.manga.dtos.ResponseDto;
import webtoon.domains.manga.entities.MangaChapterImageEntity;
import webtoon.domains.manga.models.MangaChapterImageModel;
import webtoon.domains.manga.services.IMangaChapterImageService;

@RestController
@RequestMapping("/mangaChapterImage")
public class MangaChapterImageResource {

	@Autowired
	private IMangaChapterImageService chapterImageService;

	public MangaChapterImageResource(IMangaChapterImageService chapterImageService) {
//		super();
		this.chapterImageService = chapterImageService;
	}

	@PostMapping(value = "/add")
	public ResponseDto add(@RequestBody MangaChapterImageModel model) {
		model.setId(null);
		return ResponseDto.of(this.chapterImageService.add(model));
	}

	@PostMapping(value = "/update/{id}")
	public ResponseDto update(@PathVariable Long id, @RequestBody MangaChapterImageModel model) {
		model.setId(id);
		return ResponseDto.of(this.chapterImageService.update(model));
	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable long id) {
		this.chapterImageService.deleteById(id);
	}
}
