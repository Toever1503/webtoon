package webtoon.domains.manga.resources2;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webtoon.domains.manga.models.MangaUploadChapterInput;
import webtoon.domains.manga.services.IMangaChapterService;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("api/manga/chapter")
public class MangaChapterResource2 {

    private final IMangaChapterService mangaChapterService;

    public MangaChapterResource2(IMangaChapterService mangaChapterService) {
        this.mangaChapterService = mangaChapterService;
    }

    @PostMapping("create-text-chapter")
    public void createTextChapter(@RequestBody MangaUploadChapterInput input) {
        this.mangaChapterService.createTextChapter(input);
    }

    @PostMapping("create-image-chapter")
    public Long createImageChapter(MangaUploadChapterInput input, @RequestPart List<MultipartFile> files) throws IOException {
        if (files.size() == 0)
            throw new RuntimeException("File is required");
       return this.mangaChapterService.createImageChapter(input, files);
    }

}
