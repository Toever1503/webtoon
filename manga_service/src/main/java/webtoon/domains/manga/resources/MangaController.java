package webtoon.domains.manga.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.filters.MangaFilterModel;
import webtoon.domains.manga.services.IMangaService;

@Controller
@RequestMapping("manga")
public class MangaController {

	@Autowired
	private IMangaService mangaService;
	
	@GetMapping
	public String mangaList() {
		return "trangtruyenchu";
	}

	@GetMapping("{name}/{id}")
	public String mangaDetail(@PathVariable java.lang.Long id, @PathVariable String name) {

		return "trangtruyen";
	}

	@GetMapping("{name}/chapter/{id}")
	public String readMangaChapter(@PathVariable java.lang.Long id, @PathVariable String name) {

		return "read-manga-page";
	}
	@PostMapping("/index")
	public String showMangaList(Model model,Pageable pageable,@RequestBody MangaFilterModel filterModel  ) {
		Specification<MangaEntity> specification = (root, query, criteriaBuilder) -> {
			return criteriaBuilder.or(criteriaBuilder.like(root.get("title"), "%" + filterModel.getTitle() + "%"),
					criteriaBuilder.like(root.get("mangaName"), "%" + filterModel.getMangaName() + "%"),
					criteriaBuilder.like(root.get("concerpt"), "%" + filterModel.getConcerpt() + "%")

			);
		};
		model.addAttribute("model", mangaService.filter(pageable, Specification.where(specification)));
//		model.addAttribute("users", userRepository.findAll());
		return "trangtruyenchu";
	}

}
