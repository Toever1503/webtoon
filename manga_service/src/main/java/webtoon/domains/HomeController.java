package webtoon.domains;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webtoon.account.entities.UserEntity;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.entities.MangaGenreEntity;
import webtoon.domains.post.service.ICategoryService;
import webtoon.domains.post.service.IPostService;
import webtoon.domains.manga.services.IMangaChapterService;
import webtoon.domains.manga.services.IMangaGenreService;
import webtoon.domains.manga.services.IMangaService;
import webtoon.domains.post.entities.PostEntity;

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

    @RequestMapping("/index")
    public String homepage(Model model, Pageable pageable, @RequestParam(required = false, name = "login-type") Integer hasLogged, HttpSession session) {
        Page<MangaEntity> mangaEntity = this.mangaService.filterEntities(pageable, null);

//        List<CategoryEntity> categoryEntity = this.categoryService.findAllCate();

        List<MangaGenreEntity> mangaGenreEntity = this.mangaGenreService.findAllGenre();

        List<PostEntity> postEntity = this.postService.findAllPost();

        model.addAttribute("genreList", mangaGenreEntity);
//        model.addAttribute("cateList", categoryEntity);
        model.addAttribute("mangalist", mangaEntity.getContent());
        model.addAttribute("postList", postEntity);

        if (hasLogged != null) {
            if (hasLogged == 1)
                model.addAttribute("hasLogged", "Đăng ký thành công!");
            else
                model.addAttribute("hasLogged", "Đăng nhập thành công!");
        }

        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        return "homepage";
    }
}