package webtoon.main.domains;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.domains.manga.entities.MangaEntity;
import webtoon.main.domains.manga.entities.MangaEntity_;
import webtoon.main.domains.manga.entities.MangaGenreEntity;
import webtoon.main.domains.manga.entities.ReadHistory;
import webtoon.main.domains.manga.enums.EMangaSTS;
import webtoon.main.domains.manga.enums.EStatus;
import webtoon.main.domains.manga.services.IReadHistoryService;
import webtoon.main.domains.manga.services.IMangaAuthorService;
import webtoon.main.domains.post.service.ICategoryService;
import webtoon.main.domains.post.service.IPostService;
import webtoon.main.domains.manga.services.IMangaChapterService;
import webtoon.main.domains.manga.services.IMangaGenreService;
import webtoon.main.domains.manga.services.IMangaService;
import webtoon.main.domains.post.entities.PostEntity;
import webtoon.main.domains.tag.service.ITagService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {

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

        @Autowired
        private IMangaAuthorService mangaAuthorService;

        @Autowired
        private ITagService tagService;

        public HomeController() {
                System.out.printf("HomeController init\n");
        }

        @RequestMapping(value = { "/", "/index" })
        public String homepage(Model model, Pageable pageable,
                        @RequestParam(required = false, name = "loginType") Integer hasLogged, HttpSession session) {
                Specification latestMangaSpec = Specification.where(
                                (root, query, cb) -> cb.equal(root.get(MangaEntity_.STATUS), EStatus.DRAFTED).not())
                                .and((root, query, cb) -> cb.isNull(root.get(MangaEntity_.DELETED_AT)));

                Specification mangaSpecFree = Specification.where(
                                (root, query, cb) -> cb.equal(root.get(MangaEntity_.STATUS), EStatus.DRAFTED).not())
                                .and((root, query, cb) -> cb.equal(root.get(MangaEntity_.IS_FREE), true))
                                .and((root, query, cb) -> cb.isNull(root.get(MangaEntity_.DELETED_AT)));

                Specification mangaSpecComing = Specification.where(
                                (root, query, cb) -> cb.equal(root.get(MangaEntity_.STATUS), EStatus.DRAFTED).not())
                                .and((root, query, cb) -> cb.equal(root.get(MangaEntity_.MANGA_STATUS),
                                                EMangaSTS.COMING))
                                .and((root, query, cb) -> cb.isNull(root.get(MangaEntity_.DELETED_AT)));

                List<MangaEntity> mangaSliders = this.mangaService
                                .filterEntities(PageRequest.of(0, 10, Sort.Direction.DESC, MangaEntity_.VIEW_COUNT),
                                                latestMangaSpec)
                                .getContent();
                model.addAttribute("mangaSlider", mangaSliders);

                // Page<MangaEntity> mangaEntity = this.mangaService.filterEntities(pageable,
                // mangaSpec);
                List<MangaEntity> filter5LatestMangas = this.mangaService.filter5LatestMangas(latestMangaSpec);
                List<MangaEntity> mangaEntity5Coming = this.mangaService
                                .filterEntities(PageRequest.of(0, 5), mangaSpecComing).getContent();
                List<MangaEntity> mangaEntity5Free = this.mangaService
                                .filterEntities(PageRequest.of(0, 5), mangaSpecFree).getContent();
                // List<CategoryEntity> categoryEntity = this.categoryService.findAllCate();

                List<MangaGenreEntity> mangaGenreEntity = this.mangaGenreService.findAllGenre();

                List<PostEntity> postEntity = this.postService.findAllPost();

                model.addAttribute("genreList", mangaGenreEntity);
                model.addAttribute("authorList",
                                mangaAuthorService.filterAuthor(null, Pageable.unpaged()).getContent());
                model.addAttribute("tagList", tagService.findAll(Pageable.unpaged()));
                // model.addAttribute("cateList", categoryEntity);
                // model.addAttribute("mangalist", mangaEntity.getContent());
                model.addAttribute("mangalist", filter5LatestMangas);
                model.addAttribute("mangalistComing", mangaEntity5Coming);
                model.addAttribute("mangalistFree", mangaEntity5Free);
                model.addAttribute("postList", postEntity);

                if (hasLogged != null) {
                        if (hasLogged == 1)
                                model.addAttribute("hasLogged", "Đăng ký thành công!");
                        else
                                model.addAttribute("hasLogged", "Đăng nhập thành công!");
                }

                UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
                model.addAttribute("logger", loggedUser);

                return "homepage";
        }

        @RequestMapping("contact")
        public String contact() {
                return "contact";
        }
}