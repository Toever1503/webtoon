package webtoon.domains.manga.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.dtos.ResponseDto;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.models.MangaModel;
import webtoon.domains.manga.services.IMangaService;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/mangaTest")
public class MangaResource {

    @Autowired
    private IMangaService iMangaService;

    public MangaResource(IMangaService iMangaService) {
        this.iMangaService = iMangaService;
    }

    @PostMapping(value = "/add")
    public ResponseDto addManga(@RequestBody MangaModel mangaModel) {
        mangaModel.setId(null);
        return ResponseDto.of(this.iMangaService.add(mangaModel));
    }

    @PutMapping("/update/{id}")
    public ResponseDto updateManga(@PathVariable java.lang.Long id, @RequestBody MangaModel mangaModel) {
        mangaModel.setId(id);
        return ResponseDto.of(this.iMangaService.update(mangaModel));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id) {
        this.iMangaService.deleteById(id);
    }


	@GetMapping("/filter")
	public Page<MangaEntity> filter(@RequestParam String s, Pageable page){
		return this.iMangaService.filterBy(s, page);
	}

    @GetMapping("/getRating/{id}")
    public Double getRating(@PathVariable Long id ){
        return this.iMangaService.getRating(id);
    }
    @GetMapping("/getManga/{id}")
    public MangaDto getManga(@PathVariable Long id){
        return this.iMangaService.getByMangaId(id);
    }

}
