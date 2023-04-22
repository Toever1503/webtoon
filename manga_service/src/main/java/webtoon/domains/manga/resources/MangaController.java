package webtoon.domains.manga.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import webtoon.domains.manga.dtos.MangaChapterDto;
import webtoon.domains.manga.entities.*;
import webtoon.domains.manga.services.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("manga")
public class MangaController {

	@Autowired
	@Lazy
	private  IMangaService mangaService;

	@Autowired
	private IMangaVolumeService mangaVolumeService;

	@Autowired
	private IMangaChapterService mangaChapterService;


	private final IMangaRatingService ratingService;

	private final IReadHistoryService historyService;

	private final IMangaGenreService mangaGenreService;

	public MangaController( IMangaRatingService ratingService, IReadHistoryService historyService, IMangaGenreService mangaGenreService) {

		this.ratingService = ratingService;
		this.historyService = historyService;
		this.mangaGenreService = mangaGenreService;
	}

	@GetMapping
	public String mangaList(Model model,Pageable pageable) {
		Page<MangaEntity> mangaEntities = this.mangaService.filterEntities(pageable,null);
		List<MangaGenreEntity> mangaGenreEntity = this.mangaGenreService.findAllGenre();

		model.addAttribute("genreList", mangaGenreEntity);
		model.addAttribute("mangalist", mangaEntities.getContent());
		return "trangmanga";
	}

	@GetMapping("{name}/{id}")
	public String mangaDetail(@PathVariable java.lang.Long id, @PathVariable String name,Model model) {
		MangaEntity mangaEntity =this.mangaService.getById(id);

		List<MangaChapterEntity> mangaChapter = this.mangaChapterService.findAllByMangaId(id);

//		hiển thị số sao và sô bản ghi rating
		List<Map> list = this.ratingService.getRating(id);

		model.addAttribute("modelchapter",mangaChapter);
		model.addAttribute("model",mangaEntity);
		model.addAttribute("rating",list);
			return "trangtruyen";
	}

	@GetMapping("{name}/chapter/{id}")
	public String readMangaChapter(@PathVariable java.lang.Long id, @PathVariable String name,Model model) {
		MangaChapterEntity chapterEntity = this.mangaChapterService.getById(id);
		MangaEntity mangaEntity = null;
		if(chapterEntity.getMangaVolume() == null){ // display type chap
			mangaEntity = chapterEntity.getManga();

			MangaChapterDto[] prevNextChapters = this.mangaChapterService
					.findNextPrevChapterForDisplayChapType(id,chapterEntity.getManga().getId());

			model.addAttribute("chapterData1", mangaEntity.getChapters());

			model.addAttribute("prevChapter",prevNextChapters[0]);
			model.addAttribute("nextChapter",prevNextChapters[1]);
		}
		else { // display type vol
			MangaVolumeEntity volumeEntity = chapterEntity.getMangaVolume();
			mangaEntity = volumeEntity.getManga();

			model.addAttribute("currentVol", volumeEntity);

			model.addAttribute("volumeEntity1", mangaVolumeService.findByManga(mangaEntity.getId()));
			model.addAttribute("chapterData1",mangaChapterService.findByVolume(volumeEntity.getId()));
			MangaChapterDto[] prevNextChapter = this.mangaChapterService
					.findNextPrevChapterForDisplayVolType(id,chapterEntity.getMangaVolume().getManga().getId());
			model.addAttribute("prevChapter",prevNextChapter[0]);
			model.addAttribute("nextChapter",prevNextChapter[1]);
		}
//		ReadHistory readHistory = new ReadHistory();
//		readHistory = historyService.findByCBAndMG(null,mangaEntity.getId());
//		if(readHistory != null){
//			readHistory.setChapterEntity(id);
//		} else {
//			readHistory.setMangaEntity(mangaEntity.getId());
//			readHistory.setChapterEntity(id);
//			readHistory.setCreatedBy(null);
//		}
//		historyService.save(readHistory);

		model.addAttribute("mangaData",mangaEntity);
		model.addAttribute("mangaType",mangaEntity.getMangaType().name());
		model.addAttribute("chapterData",chapterEntity);
		return "read-manga-page";
	}



}
