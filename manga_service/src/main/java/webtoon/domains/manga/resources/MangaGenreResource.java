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

import webtoon.domains.manga.dtos.ResponseDto;
import webtoon.domains.manga.entities.MangaGenreEntity;
import webtoon.domains.manga.filters.MangaGenreFilter;
import webtoon.domains.manga.models.MangaGenreModel;
import webtoon.domains.manga.services.IMangaGenreService;

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

	@PutMapping(value = "/update/{id}")
	public ResponseDto update(@PathVariable Long id, @RequestBody MangaGenreModel model) {
		model.setId(id);
		return ResponseDto.of(this.genreService.update(model));
	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable long id) {
		this.genreService.deleteById(id);
	}

	@PostMapping("filter")
	public ResponseDto filter(@RequestBody MangaGenreFilter filterModel, Pageable pageable) {
		Specification<MangaGenreEntity> specification = (root, query, criteriaBuilder) -> {
			return criteriaBuilder.or(criteriaBuilder.like(root.get("name"), "%" + filterModel.getName() + "%"),
					criteriaBuilder.like(root.get("slug"), "%" + filterModel.getSlug() + "%")

			);
		};
		return ResponseDto.of(this.genreService.filter(pageable, Specification.where(specification)));
	}
}
