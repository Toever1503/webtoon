package webtoon.domains.manga.resources;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import webtoon.account.entities.UserEntity;
import webtoon.domains.manga.dtos.MangaChapterDto;
import webtoon.domains.manga.entities.*;
import webtoon.domains.manga.enums.EMangaDisplayType;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.services.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("manga")
public class MangaController {

    @Autowired
    @Lazy
    private IMangaService mangaService;

    @Autowired
    private IMangaVolumeService mangaVolumeService;

    @Autowired
    private IMangaChapterService mangaChapterService;

    @Autowired
    @Lazy
    private IReadHistoryService historyService;


    private final IMangaRatingService ratingService;

    private final IMangaGenreService mangaGenreService;

    public MangaController(IMangaRatingService ratingService, IMangaGenreService mangaGenreService) {

        this.ratingService = ratingService;
        this.mangaGenreService = mangaGenreService;
    }

    @GetMapping("list-manga")
    public String mangaList(Model model, Pageable pageable) {
        Page<MangaEntity> mangaEntities = this.mangaService.filterEntities(pageable, null);
        List<MangaGenreEntity> mangaGenreEntity = this.mangaGenreService.findAllGenre();

        model.addAttribute("genreList", mangaGenreEntity);
        model.addAttribute("mangalist", mangaEntities.getContent());
        return "trangmanga";
    }

    @GetMapping("{name}/{id}")
    public String mangaDetail(@PathVariable java.lang.Long id, @PathVariable String name, Model model, HttpSession session) {
        this.mangaService.increaseView(id);// tang view
        MangaEntity mangaEntity = this.mangaService.getById(id);

        UserEntity userEntity = (UserEntity) session.getAttribute("loggedUser");
        if (userEntity != null) {
            ReadHistory readHistory = this.historyService.findByCBAndMG(userEntity.getId(), mangaEntity.getId());
            if (readHistory != null) {
                MangaChapterEntity mangaChapterEntity = this.mangaChapterService.getById(readHistory.getChapterEntity());
                model.addAttribute("chapterHistory", mangaChapterEntity);
                System.out.println(mangaChapterEntity);
            }
        }

        // tinh tong so tap va chuong
        if (mangaEntity.getDisplayType().equals(EMangaDisplayType.VOL)) {
            model.addAttribute("totalVolCount", this.mangaVolumeService.countTotalVol(mangaEntity.getId()));
            model.addAttribute("totalChapterCount", this.mangaChapterService.countTotalChapterForMangaTypeVol(mangaEntity.getId()));
        } else
            model.addAttribute("totalChapterCount", mangaEntity.getChapters().size());


//		hiển thị số sao và sô bản ghi ratingy
        List<Map> list = this.ratingService.getRating(mangaEntity.getId());
        Map<EMangaSTS, String> mangaStatusMap = new HashMap<>();
        mangaStatusMap.put(EMangaSTS.COMING, "Đang bắt đầu");
        mangaStatusMap.put(EMangaSTS.GOING, "Đang thực hiện");
        mangaStatusMap.put(EMangaSTS.STOPPED, "Đã dừng");
        mangaStatusMap.put(EMangaSTS.CANCELLED, "Bị hủy");
        mangaStatusMap.put(EMangaSTS.COMPLETED, "Đã hoàn thành");

        String mangaSTSMap = mangaStatusMap.get(mangaEntity.getMangaStatus());

        model.addAttribute("model", mangaEntity);
        model.addAttribute("rating", list);
        model.addAttribute("logger", userEntity);
        model.addAttribute("trangThai", mangaSTSMap);
        return "trangtruyen";
    }

    @GetMapping("{name}/chapter/{id}")
    public String readMangaChapter(@PathVariable java.lang.Long id, @PathVariable String name, Model model, HttpSession session) {
        MangaChapterEntity chapterEntity = this.mangaChapterService.getById(id);
        MangaEntity mangaEntity = null;
        if (chapterEntity.getMangaVolume() == null) { // display type chap
            mangaEntity = chapterEntity.getManga();

            MangaChapterDto[] prevNextChapters = this.mangaChapterService
                    .findNextPrevChapterForDisplayChapType(id, chapterEntity.getManga().getId());

            model.addAttribute("chapterData1", mangaEntity.getChapters());

            model.addAttribute("prevChapter", prevNextChapters[0]);
            model.addAttribute("nextChapter", prevNextChapters[1]);

        } else { // display type vol
            MangaVolumeEntity volumeEntity = chapterEntity.getMangaVolume();
            mangaEntity = volumeEntity.getManga();

            model.addAttribute("currentVol", volumeEntity);

            model.addAttribute("volumeEntity1", mangaVolumeService.findByManga(mangaEntity.getId()));
            model.addAttribute("chapterData1", mangaChapterService.findByVolume(volumeEntity.getId()));
            MangaChapterDto[] prevNextChapter = this.mangaChapterService
                    .findNextPrevChapterForDisplayVolType(id, chapterEntity.getMangaVolume().getManga().getId());
            model.addAttribute("prevChapter", prevNextChapter[0]);
            model.addAttribute("nextChapter", prevNextChapter[1]);
        }

        // tang view len 1
        this.mangaService.increaseView(mangaEntity.getId());

        ReadHistory readHistory = new ReadHistory();

        UserEntity logger = (UserEntity) session.getAttribute("loggedUser");
        if (logger != null) {
            readHistory = historyService.findByCBAndMG(logger.getId(), mangaEntity.getId());
            if (readHistory != null) {

                readHistory.setChapterEntity(chapterEntity.getId());
                historyService.save(readHistory);
            } else {
                ReadHistory readHistory1 = new ReadHistory();
                readHistory1.setMangaEntity(mangaEntity.getId());
                readHistory1.setChapterEntity(chapterEntity.getId());
                readHistory1.setCreatedBy(logger.getId());
                historyService.save(readHistory1);
            }
        }
        model.addAttribute("mangaData", mangaEntity);
        model.addAttribute("mangaType", mangaEntity.getMangaType().name());
        model.addAttribute("chapterData", chapterEntity);
        model.addAttribute("logger", logger);
        return "read-manga-page";
    }


}
