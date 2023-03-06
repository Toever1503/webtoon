package webtoon.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import webtoon.dtos.ResponseDto;
import webtoon.models.MangaModel;
import webtoon.services.IMangaService;

@RestController
public class MangaResource {

	@Autowired
	private IMangaService iMangaService;

	public MangaResource(IMangaService iMangaService) {
		this.iMangaService = iMangaService;
	}
	
	@PostMapping("/add")
	public ResponseDto addManga(@RequestBody MangaModel mangaModel) {
		mangaModel.setId(null);
		return ResponseDto.of(this.iMangaService.add(mangaModel));
	}
	
}
