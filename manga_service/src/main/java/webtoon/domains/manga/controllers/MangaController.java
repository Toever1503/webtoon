package webtoon.domains.manga.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import webtoon.account.entities.UserEntity;
import webtoon.domains.manga.dtos.MangaChapterDto;
import webtoon.domains.manga.dtos.MangaDto;
import webtoon.domains.manga.entities.*;
import webtoon.domains.manga.enums.EMangaDisplayType;
import webtoon.domains.manga.enums.EMangaSTS;
import webtoon.domains.manga.enums.EStatus;
import webtoon.domains.manga.services.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        Specification mangaSpec = Specification.where(
                (root, query, cb) -> cb.equal(root.get(MangaEntity_.STATUS), EStatus.DRAFTED).not()
        ).and((root, query, cb) -> cb.isNull(root.get(MangaEntity_.DELETED_AT)));

        Page<MangaEntity> mangaEntities = this.mangaService.filterEntities(pageable, mangaSpec);
        List<MangaGenreEntity> mangaGenreEntity = this.mangaGenreService.findAllGenre();

        model.addAttribute("genreList", mangaGenreEntity);
        model.addAttribute("mangalist", mangaEntities.getContent());


        model.addAttribute("hasPrevPage", mangaEntities.hasPrevious());
        model.addAttribute("hasNextPage", mangaEntities.hasNext());
        model.addAttribute("currentPage", mangaEntities.getNumber());
        model.addAttribute("totalPage", mangaEntities.getTotalPages());
        return "trangmanga";
    }

    @GetMapping("{name}/{id}")
    public String mangaDetail(@PathVariable java.lang.Long id,
                              @PathVariable String name,
                              Model model,
                              HttpSession session,
                              Pageable pageable) {
        MangaEntity mangaEntity = this.mangaService.getById(id);
        if (mangaEntity.getDeletedAt() != null) {
            throw new RuntimeException("manga has been deleted");
        }
        this.mangaService.increaseView(id);// tang view

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
        mangaStatusMap.put(EMangaSTS.ON_STOPPING, "Đang tạm dừng");
        mangaStatusMap.put(EMangaSTS.COMPLETED, "Đã hoàn thành");

        String mangaSTSMap = mangaStatusMap.get(mangaEntity.getMangaStatus());
        model.addAttribute("model", mangaEntity);
        model.addAttribute("rating", list);
        model.addAttribute("logger", userEntity);
        model.addAttribute("trangThai", mangaSTSMap);


        // for related manga
        List<MangaEntity> recentMangaList = this.mangaService.getALLByGenres(mangaEntity.getGenres().stream().map(MangaGenreEntity::getId).collect(Collectors.toList()));
        model.addAttribute("relatedMangas", recentMangaList);


        if (mangaEntity.getDisplayType().equals(EMangaDisplayType.VOL)) {
            Specification spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join(MangaVolumeEntity_.MANGA).get(MangaEntity_.ID), mangaEntity.getId());
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()).withSort(Sort.Direction.DESC, "id");
            Page<MangaVolumeEntity> volumeEntities = this.mangaVolumeService.filterEntity(pageable, Specification.where(spec));

            model.addAttribute("volumeEntities", volumeEntities.getContent());

            model.addAttribute("hasPrevPage", volumeEntities.hasPrevious());
            model.addAttribute("hasNextPage", volumeEntities.hasNext());
            model.addAttribute("currentPage", volumeEntities.getNumber());
            model.addAttribute("totalPage", volumeEntities.getTotalPages());
        } else {
            Specification spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join(MangaChapterEntity_.MANGA).get(MangaEntity_.ID), mangaEntity.getId());
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()).withSort(Sort.Direction.DESC, "id");
            Page<MangaChapterEntity> chapterEntities = this.mangaChapterService.filter(pageable, Specification.where(spec));
            model.addAttribute("chapterEntities", chapterEntities.getContent());

            model.addAttribute("hasPrevPage", chapterEntities.hasPrevious());
            model.addAttribute("hasNextPage", chapterEntities.hasNext());
            model.addAttribute("currentPage", chapterEntities.getNumber());
            model.addAttribute("totalPage", chapterEntities.getTotalPages());
        }


        return "trangtruyen";
    }

    @GetMapping("{name}/chapter/{id}")
    public String readMangaChapter(@PathVariable java.lang.Long id, @PathVariable String name, Model model, HttpSession session) {
        MangaChapterEntity chapterEntity = this.mangaChapterService.getById(id);
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (chapterEntity.getRequiredVip() == true) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            if (loggedUser == null && loggedUser.getCanReadUntilDate() != null) {
                throw new RuntimeException("you need to login");
            }
            int result = formatter.format(loggedUser.getCanReadUntilDate()).compareTo(formatter.format(Calendar.getInstance().getTime()));
            if (result < 0) {
                throw new RuntimeException("you need to buy subscription");
            }
        }

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

        if (loggedUser != null) {
            readHistory = historyService.findByCBAndMG(loggedUser.getId(), mangaEntity.getId());
            if (readHistory != null) {

                readHistory.setChapterEntity(chapterEntity.getId());
                historyService.save(readHistory);
            } else {
                ReadHistory readHistory1 = new ReadHistory();
                readHistory1.setMangaEntity(mangaEntity.getId());
                readHistory1.setChapterEntity(chapterEntity.getId());
                readHistory1.setCreatedBy(loggedUser.getId());
                historyService.save(readHistory1);
            }
        }
        model.addAttribute("mangaData", mangaEntity);
        model.addAttribute("mangaType", mangaEntity.getMangaType().name());
        model.addAttribute("chapterData", chapterEntity);
        model.addAttribute("logger", loggedUser);
        return "read-manga-page";
    }


}
