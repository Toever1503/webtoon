package webtoon.domains.manga.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.services.IMangaService;

@Controller
@RequestMapping("/manga")
public class MangaController {
	@Autowired
	private IMangaService mangaService;

	@GetMapping("/index")
	public String showMangaList(Model model, Pageable page, @RequestParam String s) {
		model.addAttribute("model", mangaService.filter(s, page));
		return "trangtruyenchu";
	}

	
	
	@GetMapping
	public String mangaList() {
		return "trangtruyenchu";
	}

	@GetMapping("{name}/{id}")
	public String mangaDetail(@PathVariable Long id, @PathVariable String name) {

		return "trangtruyen";
	}

	@GetMapping("{name}/chapter/{id}")
	public String readMangaChapter(@PathVariable Long id, @PathVariable String name, Model model) {
		MangaDto entity = mangaService.findByID(id);
		
		
		model.addAttribute("model", entity);
		return "read-manga-page";
	}

}
