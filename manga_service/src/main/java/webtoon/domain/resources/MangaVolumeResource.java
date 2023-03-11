package webtoon.domain.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import webtoon.domain.dtos.ResponseDto;
import webtoon.domain.models.MangaVolumeModel;
import webtoon.domain.services.IMangaVolumeService;

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
	public ResponseDto update(@PathVariable Long id,@RequestBody MangaVolumeModel model) {
		model.setId(id);
		return ResponseDto.of(this.volumeService.update(model));
	}
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable Long id) {
		this.volumeService.deleteById(id);
	}
	
}
