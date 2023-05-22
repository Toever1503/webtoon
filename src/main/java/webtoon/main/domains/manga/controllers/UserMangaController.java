package webtoon.main.domains.manga.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.domains.manga.dtos.ReadHistoryDto2;
import webtoon.main.domains.manga.services.IReadHistoryService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserMangaController {

    @Autowired
    private IReadHistoryService readHistoryService;

    @GetMapping("read-history")
    public String readHistory(Model model,
                              HttpSession session,
                              Pageable pageable) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/signin?redirect=/user/read-history";
        }

        Page<ReadHistoryDto2> readHistoryDto2Page = this.readHistoryService.findAllByCreatedBy(loggedUser.getId(), PageRequest.of(pageable.getPageNumber(), 10).withSort(Sort.Direction.DESC, "createdDate"));

        model.addAttribute("mangaList", readHistoryDto2Page.getContent());
        model.addAttribute("currentTab", "read-history");

        model.addAttribute("hasPrevPage", readHistoryDto2Page.hasPrevious());
        model.addAttribute("hasNextPage", readHistoryDto2Page.hasNext());
        model.addAttribute("currentPage", readHistoryDto2Page.getNumber());
        model.addAttribute("totalPage", readHistoryDto2Page.getTotalPages());
        return "manga/user/read_history";
    }
}
