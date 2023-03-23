package webtoon.domains.manga.resources;

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

import webtoon.domains.manga.dtos.MangaGenreRelationDto;
import webtoon.domains.manga.dtos.ResponseDto;
import webtoon.domains.manga.entities.MangaGenreRelationEntity;
import webtoon.domains.manga.models.MangaGenreRelationModel;
import webtoon.domains.manga.services.IMangaGenreRelationService;

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

	@PutMapping(value = "/update/{id}")
	public ResponseDto update(@PathVariable Long id, @RequestBody MangaGenreRelationModel model) {
		model.setId(id);
		return ResponseDto.of(this.genreRelationService.update(model));
	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable long id) {
		this.genreRelationService.deleteById(id);
	}

	@PostMapping("/filter")
	public ResponseDto filter(@RequestBody MangaGenreRelationDto filterModel, Pageable pageable) {
		Specification<MangaGenreRelationEntity> specification = (root, query, criteriaBuilder) -> {
			return criteriaBuilder.or(criteriaBuilder.like(root.get("genreId"), "%" + filterModel.getGenreId() + "%")

			);
		};
		return ResponseDto.of(this.genreRelationService.filter(pageable, Specification.where(specification)));
	}
}
