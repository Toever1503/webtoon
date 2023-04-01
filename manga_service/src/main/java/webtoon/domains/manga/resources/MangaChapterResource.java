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

import webtoon.domains.manga.dtos.MangaChapterDto;
import webtoon.domains.manga.dtos.ResponseDto;
import webtoon.domains.manga.entities.MangaChapterEntity;
import webtoon.domains.manga.models.MangaChapterModel;
import webtoon.domains.manga.services.IMangaChapterService;

@RestController
@RequestMapping("/mangachapter")
public class MangaChapterResource {

	@Autowired
	private IMangaChapterService chapterService;

	public MangaChapterResource(IMangaChapterService chapterService) {
//		super();
		this.chapterService = chapterService;
	}

	@PostMapping(value = "/add")
	public ResponseDto add(@RequestBody MangaChapterModel model) {
		model.setId(null);
		return ResponseDto.of(this.chapterService.add(model));
	}

	@PutMapping(value = "/update/{id}")
	public ResponseDto update(@PathVariable Long id, @RequestBody MangaChapterModel model) {
		model.setId(id);
		return ResponseDto.of(this.chapterService.update(model));
	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable long id) {
		this.chapterService.deleteById(id);
	}

	@PostMapping("/filter")
	public ResponseDto filter(@RequestBody MangaChapterDto filterModel, Pageable pageable) {
		Specification<MangaChapterEntity> specification = (root, query, criteriaBuilder) -> {
			return criteriaBuilder.or(criteriaBuilder.like(root.get("name"), "%" + filterModel.getChapterName() + "%"),
					criteriaBuilder.like(root.get("content"), "%" + filterModel.getContent() + "%"),
					criteriaBuilder.like(root.get("chapterIndex"), "%" + filterModel.getChapterIndex() + "%"),
					criteriaBuilder.like(root.get("requiredVip"), "%" + filterModel.getIsRequiredVip() + "%")

			);
		};
		return ResponseDto.of(this.chapterService.filter(pageable, Specification.where(specification)));
	}

}
