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
import webtoon.domains.manga.entities.MangaVolumeEntity;
import webtoon.domains.manga.entities.MangaVolumeEntity_;
import webtoon.domains.manga.filters.MangaVolumeFilter;
import webtoon.domains.manga.models.MangaVolumeModel;
import webtoon.domains.manga.services.IMangaVolumeService;

@RestController
@RequestMapping("/mangavolume")
public class MangaVolumeResource {
	@Autowired
	private IMangaVolumeService volumeService;

	public MangaVolumeResource(IMangaVolumeService volumeService) {
		this.volumeService = volumeService;
	}

	@PostMapping("/add")
	public ResponseDto add(@RequestBody MangaVolumeModel model) {
		model.setId(null);
		return ResponseDto.of(this.volumeService.add(model));
	}

	@PutMapping("/update")
	public ResponseDto update(@PathVariable Long id, @RequestBody MangaVolumeModel model) {
		model.setId(id);
		return ResponseDto.of(this.volumeService.update(model));
	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable Long id) {
		this.volumeService.deleteById(id);
	}

	@PostMapping("/filter")
	public ResponseDto filter(@RequestBody MangaVolumeFilter filterModel, Pageable pageable) {
		Specification<MangaVolumeEntity> specification = (root, query, criteriaBuilder) -> {
			return criteriaBuilder.or(criteriaBuilder.like(root.get(MangaVolumeEntity_.NAME), "%" + filterModel.getName() + "%"),
					criteriaBuilder.like(root.get(MangaVolumeEntity_.VOLUME_INDEX), "%" + filterModel.getVolumeIndex() + "%")

			);
		};
		return ResponseDto.of(this.volumeService.filter(pageable, Specification.where(specification)));
	}

}
