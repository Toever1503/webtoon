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

import webtoon.domains.manga.dtos.MangaAuthorRelationDto;
import webtoon.domains.manga.dtos.ResponseDto;
import webtoon.domains.manga.entities.MangaAuthorRelationEntity;
import webtoon.domains.manga.models.MangaAuthorRelationModel;
import webtoon.domains.manga.services.IMangaAuthorRelationService;

@RestController
@RequestMapping("/mangaAuthorRelation")
public class MangaAuthorRelationResource {

	@Autowired
	private IMangaAuthorRelationService authorService;

	public MangaAuthorRelationResource(IMangaAuthorRelationService authorService) {
//		super();
		this.authorService = authorService;
	}

	@PostMapping(value = "/add")
	public ResponseDto add(@RequestBody MangaAuthorRelationModel model) {
		model.setId(null);
		return ResponseDto.of(this.authorService.add(model));
	}

	@PutMapping(value = "/update/{id}")
	public ResponseDto update(@PathVariable Long id, @RequestBody MangaAuthorRelationModel model) {
		model.setId(id);
		return ResponseDto.of(this.authorService.update(model));
	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable long id) {
		this.authorService.deleteById(id);
	}

	@PostMapping("/filter")
	public ResponseDto filter(@RequestBody MangaAuthorRelationDto filterModel, Pageable pageable) {
		Specification<MangaAuthorRelationEntity> specification = (root, query, criteriaBuilder) -> {
			return criteriaBuilder
					.or(criteriaBuilder.like(root.get("authorType"), "%" + filterModel.getAuthorType() + "%")

					);
		};
		return ResponseDto.of(this.authorService.filter(pageable, Specification.where(specification)));
	}
}
