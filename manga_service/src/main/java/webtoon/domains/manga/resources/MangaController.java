package webtoon.domains.manga.resources;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("manga")
public class MangaController {

	@GetMapping
	public String mangaList() {
		return "trangtruyenchu";
	}

	@GetMapping("{name}/{id}")
	public String mangaDetail(@PathVariable Long id, @PathVariable String name) {

		return "trangtruyen";
	}

	@GetMapping("{name}/chapter/{id}")
	public String readMangaChapter(@PathVariable Long id, @PathVariable String name) {

		return "read-manga-page";
	}

}
