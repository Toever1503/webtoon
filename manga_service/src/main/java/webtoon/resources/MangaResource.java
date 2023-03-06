package webtoon.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import webtoon.dtos.ResponseDto;
import webtoon.entities.MangaEntity;
import webtoon.models.MangaFilterModel;
import webtoon.models.MangaModel;
import webtoon.services.IMangaService;

@RestController
@RequestMapping("/manga")
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
	
	@PutMapping("/update")
	public ResponseDto updateManga(@PathVariable Long id,@RequestBody MangaModel mangaModel) {
		mangaModel.setId(id);
		return ResponseDto.of(this.iMangaService.update(mangaModel));
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable long id) {
		this.iMangaService.deleteById(id);
	}
	
	@PostMapping("/filter")
	public ResponseDto filter(@RequestBody MangaFilterModel filterModel,Pageable pageable) {
		Specification<MangaEntity> specification = (root, query, criteriaBuilder) -> {
			return criteriaBuilder.or(
					criteriaBuilder.like(root.get("title"), "%" + filterModel.getTitle() + "%" ),
					criteriaBuilder.like(root.get("mangaName"), "%" + filterModel.getMangaName() + "%" ),
					criteriaBuilder.like(root.get("concerpt"), "%" + filterModel.getConcerpt() + "%" )
					
			);
		};
	        return ResponseDto.of(this.iMangaService.filter(pageable, Specification.where(specification)));
	}
}
