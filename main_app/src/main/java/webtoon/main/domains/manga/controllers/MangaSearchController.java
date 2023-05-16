package webtoon.main.domains.manga.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webtoon.main.domains.manga.entities.MangaEntity;
import webtoon.main.domains.manga.entities.MangaEntity_;
import webtoon.main.domains.manga.entities.MangaGenreEntity;
import webtoon.main.domains.manga.enums.EMangaSTS;
import webtoon.main.domains.manga.enums.EStatus;
import webtoon.main.domains.manga.models.MangaFilterInput;
import webtoon.main.domains.manga.services.IMangaGenreService;
import webtoon.main.domains.manga.services.IMangaService;
import webtoon.main.domains.manga.specifications.SearchSpecification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MangaSearchController {
    @Autowired
    @Lazy
    private IMangaGenreService genreService;

    @Autowired
    @Lazy
    private IMangaService mangaService;

    @GetMapping("/search")
    public String getSearch(Model model, Pageable pageable, @RequestParam(required = false) String q,
                            @RequestParam(required = false) Long genreId,
                            @RequestParam(required = false) EMangaSTS status,
                            @RequestParam(required = false) Integer releaseYear ){
        MangaFilterInput filterInput = new MangaFilterInput();
        filterInput.setQ(q);
        filterInput.setGenreId(genreId);
        filterInput.setReleaseYear(releaseYear);
        filterInput.setStatus(status);
        Page<MangaEntity> mangaEntity = this.mangaService.filterEntities(pageable, SearchSpecification.filter(filterInput));
        List<MangaGenreEntity> mangaGenre =  this.genreService.findAllGenre();

        HashMap<EMangaSTS, String> mangaStatusMap = new HashMap<>();
        mangaStatusMap.put(EMangaSTS.COMING, "Đang bắt đầu");
        mangaStatusMap.put(EMangaSTS.GOING, "Đang thực hiện");
        mangaStatusMap.put(EMangaSTS.ON_STOPPING, "Đang tạm dừng");
        mangaStatusMap.put(EMangaSTS.COMPLETED, "Đã hoàn thành");


        model.addAttribute("filterInput",filterInput);
        model.addAttribute("mangaEntity",mangaEntity);
        model.addAttribute("mangaGenre",mangaGenre);
        model.addAttribute("trangThai", mangaStatusMap);

        return "/manga/search";
    }

}
