package webtoon.domains;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import webtoon.domains.manga.dtos.MangaChapterDto;
import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.dtos.MangaVolumeDto;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.entities.MangaGenreEntity;
import webtoon.domains.manga.entities.MangaVolumeEntity;
import webtoon.domains.manga.enums.EMangaDisplayType;
import webtoon.domains.manga.services.IMangaChapterService;
import webtoon.domains.manga.services.IMangaGenreService;
import webtoon.domains.manga.services.IMangaService;
import webtoon.domains.post.entities.CategoryEntity;
import webtoon.domains.post.entities.PostEntity;
import webtoon.domains.post.service.ICategoryService;
import webtoon.domains.post.service.IPostService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomepageController {

    @Autowired
    private IMangaService mangaService;

    @Autowired
    private IMangaChapterService chapterService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IMangaGenreService mangaGenreService;

    @Autowired
    private IPostService postService;

    public HomepageController(IMangaChapterService chapterService) {
        this.chapterService = chapterService;
    }

    public HomepageController(IMangaService mangaService) {
        this.mangaService = mangaService;
    }

    //	private final Logger logger = LoggerFactory
    public HomepageController() {
        super();
        System.out.println("homepage controller created!");
        // TODO Auto-generated constructor stub
    }

    @RequestMapping("/index")
    public String homepage(Model model, Pageable pageable) {
        Page<MangaEntity> mangaEntity = this.mangaService.filterEntities(pageable, null);

        List<CategoryEntity> categoryEntity = this.categoryService.findAllCate();

        List<MangaGenreEntity> mangaGenreEntity = this.mangaGenreService.findAllGenre();

        List<PostEntity> postEntity = this.postService.findAllPost();


        mangaEntity.forEach(e -> {
            System.out.println("manga name: " + e.getTitle());
            if (e.getDisplayType().equals(EMangaDisplayType.VOL))
                System.out.println("total volume: " + e.getVolumeEntities().size());
            else
                System.out.println("total chapter: " + e.getChapters().size());
        });
        model.addAttribute("genreList", mangaGenreEntity);
        model.addAttribute("cateList", categoryEntity);
        model.addAttribute("mangalist", mangaEntity.getContent());
        model.addAttribute("postList", postEntity);
        return "homepage";
    }
}
