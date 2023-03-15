package webtoon.domains.manga.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import webtoon.domains.manga.dtos.ResponseDto;
import webtoon.domains.manga.models.MangaChapterModel;
import webtoon.domains.manga.services.IMangaChapterService;

@RestController
@RequestMapping("/mangachapter")
public class MangaChapterResource {

	@Autowired
	private IMangaChapterService chapterService;

	public MangaChapterResource(IMangaChapterService chapterService) {
//		super();
		this.chapterService = chapterService;
	}

	@PostMapping(value = "/add")
	public ResponseDto add(@RequestBody MangaChapterModel model) {
		model.setId(null);
		return ResponseDto.of(this.chapterService.add(model));
	}

	@PutMapping(value = "/update")
	public ResponseDto update(@PathVariable Long id, @RequestBody MangaChapterModel model) {
		model.setId(id);
		return ResponseDto.of(this.chapterService.update(model));
	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable long id) {
		this.chapterService.deleteById(id);
	}

}
