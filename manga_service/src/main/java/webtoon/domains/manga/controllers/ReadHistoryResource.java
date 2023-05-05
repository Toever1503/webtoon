package webtoon.domains.manga.controllers;

import org.springframework.web.bind.annotation.*;
import webtoon.domains.manga.dtos.ResponseDto;
import webtoon.domains.manga.entities.ReadHistory;
import webtoon.domains.manga.models.ReadHistoryModel;
import webtoon.domains.manga.services.IReadHistoryService;

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
}
