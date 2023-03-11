package webtoon.domain.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import webtoon.domain.dtos.ResponseDto;
import webtoon.domain.models.MangaAuthorModel;
import webtoon.domain.services.IMangaAuthorService;

@RestController
@RequestMapping("/mangaauthor")
public class MangaAuthorResource {
	@Autowired
	private IMangaAuthorService authorService;

	public MangaAuthorResource(IMangaAuthorService authorService) {
//		super();
		this.authorService = authorService;
	}
	
	@PostMapping(value = "/add")
	public ResponseDto add(@RequestBody MangaAuthorModel model) {
		model.setId(null);
		return ResponseDto.of(this.authorService.add(model));
	}
	
	@PostMapping(value = "/update")
	public ResponseDto update(@PathVariable Long id,@RequestBody MangaAuthorModel model) {
		model.setId(id);
		return ResponseDto.of(this.authorService.update(model));
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable long id) {
		this.authorService.deleteById(id);
	}
}
