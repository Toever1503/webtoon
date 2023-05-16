package webtoon.main.domains.manga.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import webtoon.main.domains.manga.dtos.MangaChapterDto;
import webtoon.main.domains.manga.dtos.ResponseDto;
import webtoon.main.domains.manga.entities.MangaChapterEntity_;
import webtoon.main.domains.manga.models.MangaChapterModel;
import webtoon.main.domains.manga.entities.MangaChapterEntity;
import webtoon.main.domains.manga.services.IMangaChapterService;

import java.util.List;

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
			return criteriaBuilder.or(criteriaBuilder.like(root.get(MangaChapterEntity_.CHAPTER_NAME), "%" + filterModel.getChapterName() + "%"),
					criteriaBuilder.like(root.get(MangaChapterEntity_.CONTENT), "%" + filterModel.getContent() + "%"),
					criteriaBuilder.like(root.get(MangaChapterEntity_.CHAPTER_INDEX), "%" + filterModel.getChapterIndex() + "%"),
					criteriaBuilder.like(root.get(MangaChapterEntity_.REQUIRED_VIP), "%" + filterModel.getRequiredVip() + "%")

			);
		};
		return ResponseDto.of(this.chapterService.filter(pageable, Specification.where(specification)));
	}

    @GetMapping("get-all-chapters-by-vol/{volId}")
    public List<MangaChapterDto> getAllChapterSByVol(@PathVariable  Long volId) {
        return this.chapterService.findAllByVolume(volId);
    }

}
