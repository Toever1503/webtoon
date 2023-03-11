package webtoon.domain.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import webtoon.domain.dtos.ResponseDto;
import webtoon.domain.models.MangaGenreModel;
import webtoon.domain.services.IMangaGenreService;

@RestController
@RequestMapping("/mangaGenre")
public class MangaGenreResource {
	@Autowired
	private IMangaGenreService genreService;

	public MangaGenreResource(IMangaGenreService genreService) {
//		super();
		this.genreService = genreService;
	}

	@PostMapping(value = "/add")
	public ResponseDto add(@RequestBody MangaGenreModel model) {
		model.setId(null);
		return ResponseDto.of(this.genreService.add(model));
	}

	@PutMapping(value = "/update")
	public ResponseDto update(@PathVariable Long id, @RequestBody MangaGenreModel model) {
		model.setId(id);
		return ResponseDto.of(this.genreService.update(model));
	}
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable long id) {
		this.genreService.deleteById(id);
	}
}
