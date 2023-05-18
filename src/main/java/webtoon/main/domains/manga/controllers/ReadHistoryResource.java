package webtoon.main.domains.manga.controllers;

import org.springframework.web.bind.annotation.*;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.domains.manga.dtos.ReadHistoryDto2;
import webtoon.main.domains.manga.dtos.ResponseDto;
import webtoon.main.domains.manga.entities.ReadHistory;
import webtoon.main.domains.manga.models.ReadHistoryModel;
import webtoon.main.domains.manga.services.IReadHistoryService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/history")
public class ReadHistoryResource {
    private final IReadHistoryService historyService;

    public ReadHistoryResource(IReadHistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping("/add")
    public ResponseDto add(@RequestBody ReadHistoryModel model){
        model.setId(null);
        return ResponseDto.of(this.historyService.addReadHistory(model));
    }

    @PutMapping("/update/{id}")
    public ResponseDto update(@PathVariable Long id,@RequestBody ReadHistoryModel model){
        model.setId(id);
        return ResponseDto.of(this.historyService.update(model));
    }

    @DeleteMapping("/delete/{id}")
    public void  delete(@PathVariable Long id){
        this.historyService.deleteById(id);
    }

//    public ReadHistory findByMangaAndCreateBy(){
//        return historyService.findByCBAndMG()
//    }

    @GetMapping("/updateReadHistoryAfterLogin/{mangaId}/{chapterId}")
    public Boolean updateReadHistoryAfterLogin(@PathVariable("mangaId") Long mangaId,@PathVariable("chapterId") Long chapterId, HttpSession session){
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser != null) {
            ReadHistory readHistory = historyService.findByCBAndMG(loggedUser.getId(), mangaId);
            if (readHistory != null) {
                readHistory.setChapterEntity(chapterId);
                this.historyService.save(readHistory);
                return true;
            } else {
                ReadHistory readHistory1 = new ReadHistory();
                readHistory1.setMangaEntity(mangaId);
                readHistory1.setChapterEntity(chapterId);
                readHistory1.setCreatedBy(loggedUser.getId());
                this.historyService.save(readHistory1);
                return true;
            }
        }
        return false;
    }

    @GetMapping("/getReadHistory/{createById}")
    public List<ReadHistoryDto2> getReadHistory(@PathVariable("createById") Long createById){
        return this.historyService.findAllByCreatedBy(createById);
    }
}
