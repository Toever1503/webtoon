package webtoon.domains.manga.resources2;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webtoon.domains.manga.models.MangaUploadChapterInput;
import webtoon.domains.manga.services.IMangaChapterService;

@RestController
@RequestMapping("api/manga/chapter")
public class MangaChapterResource2 {

    private final IMangaChapterService mangaChapterService;

    public MangaChapterResource2(IMangaChapterService mangaChapterService) {
        this.mangaChapterService = mangaChapterService;
    }

    @PostMapping("create-text-chapter")
    public void createTextChapter(@RequestBody MangaUploadChapterInput input){
        this.mangaChapterService.createTextChapter(input);
    }
}
