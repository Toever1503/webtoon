package webtoon.domains.manga.resources2;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webtoon.domains.manga.dtos.MangaChapterDto;
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

    @PostMapping("save-text-chapter")
    public MangaChapterDto saveTextChapter(@RequestBody MangaUploadChapterInput input, @RequestParam Long mangaId) {
        input.setMangaID(mangaId);
        return this.mangaChapterService.saveTextChapter(input);
    }

    @PostMapping("save-image-chapter")
    public MangaChapterDto saveImageChapter(MangaUploadChapterInput input, @RequestParam Long mangaId, @RequestPart List<MultipartFile> files) throws IOException {
        if (files.size() == 0)
            throw new RuntimeException("File is required");
        input.setMangaID(mangaId);
        return this.mangaChapterService.saveImageChapter(input, files);
    }

    @GetMapping("get-all-by-volume/{id}")
    public List<MangaChapterDto> getAllByVolumeId(@PathVariable Long id) {
        return this.mangaChapterService.getAllByVolumeId(id);
    }

}
