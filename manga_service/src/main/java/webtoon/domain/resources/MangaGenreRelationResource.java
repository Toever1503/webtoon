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
import webtoon.domain.models.MangaGenreRelationModel;
import webtoon.domain.services.IMangaGenreRelationService;

@RestController
@RequestMapping("/mangaGenreRelation")
public class MangaGenreRelationResource {
	@Autowired
	private IMangaGenreRelationService genreRelationService;


	public MangaGenreRelationResource(IMangaGenreRelationService genreRelationService) {
		super();
		this.genreRelationService = genreRelationService;
	}


	@PostMapping(value = "/add")
	public ResponseDto add(@RequestBody MangaGenreRelationModel model) {
		model.setId(null);
		return ResponseDto.of(this.genreRelationService.add(model));
	}
	
	@PutMapping(value = "/update")
	public ResponseDto update(@PathVariable Long id,@RequestBody MangaGenreRelationModel model) {
		model.setId(id);
		return ResponseDto.of(this.genreRelationService.update(model));
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable long id) {
		this.genreRelationService.deleteById(id);
	}
}
