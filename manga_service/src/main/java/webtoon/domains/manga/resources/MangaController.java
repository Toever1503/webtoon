package webtoon.domains.manga.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import webtoon.domains.manga.dtos.MangaChapterDto;
import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.dtos.MangaVolumeDto;
import webtoon.domains.manga.entities.MangaChapterEntity;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.entities.MangaVolumeEntity;
import webtoon.domains.manga.filters.MangaFilterModel;
import webtoon.domains.manga.services.IMangaChapterService;
import webtoon.domains.manga.services.IMangaService;
import webtoon.domains.manga.services.IMangaVolumeService;

@Controller
@RequestMapping("manga")
public class MangaController {

	@Autowired
	private IMangaService mangaService;

	@Autowired
	private IMangaVolumeService mangaVolumeService;

	@Autowired
	private IMangaChapterService mangaChapterService;


	@GetMapping
	public String mangaList() {
		return "trangtruyenchu";
	}

	@GetMapping("{name}/{id}")
	public String mangaDetail(@PathVariable java.lang.Long id, @PathVariable String name,Model model) {
		MangaEntity mangaEntity =this.mangaService.getById(id);
		model.addAttribute("model",mangaEntity);

		return "trangtruyen";
	}

	@GetMapping("{name}/chapter/{id}")
	public String readMangaChapter(@PathVariable java.lang.Long id, @PathVariable String name,Model model) {
		MangaChapterEntity chapterEntity = this.mangaChapterService.getById(id);
		MangaVolumeEntity volumeEntity = chapterEntity.getMangaVolume();
		MangaEntity mangaEntity = volumeEntity.getManga();

//		MangaVolumeEntity volumeEntity1 = (MangaVolumeEntity) volumeEntity.getManga().getVolumeEntities();

		model.addAttribute("mangaData",mangaEntity);
		model.addAttribute("mangaType",mangaEntity.getMangaType().name());
		model.addAttribute("chapterData",chapterEntity);

		MangaChapterDto[] prevNextChapter = this.mangaChapterService
				.findNextPosts(id,chapterEntity.getMangaVolume().getId());
		model.addAttribute("prevChapter",prevNextChapter[0]);
		model.addAttribute("nextChapter",prevNextChapter[1]);

		return "read-manga-page";
	}
	@PostMapping("/index")
	public String showMangaList(Model model,Pageable pageable,@RequestParam String s  ) {

		model.addAttribute("model", mangaService.filterBy(s,pageable));
//		model.addAttribute("users", userRepository.findAll());
		return "trangtruyenchu";
	}

}
