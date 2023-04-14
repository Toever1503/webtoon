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

import webtoon.domains.manga.dtos.MangaAuthorDto;
import webtoon.domains.manga.dtos.ResponseDto;
import webtoon.domains.manga.entities.MangaAuthorEntity_;
import webtoon.domains.manga.models.MangaAuthorModel;
import webtoon.domains.manga.entities.MangaAuthorEntity;
import webtoon.domains.manga.services.IMangaAuthorService;

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

	@PostMapping(value = "/update/{id}")
	public ResponseDto update(@PathVariable Long id, @RequestBody MangaAuthorModel model) {
		model.setId(id);
		return ResponseDto.of(this.authorService.update(model));
	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable long id) {
		this.authorService.deleteById(id);
	}

	@PostMapping("/filter")
	public ResponseDto filter(@RequestBody MangaAuthorDto filterModel, Pageable pageable) {
		Specification<MangaAuthorEntity> specification = (root, query, criteriaBuilder) -> {
			return criteriaBuilder.or(criteriaBuilder.like(root.get(MangaAuthorEntity_.NAME), "%" + filterModel.getName() + "%")

			);
		};
		return ResponseDto.of(this.authorService.filter(pageable, Specification.where(specification)));
	}
}
