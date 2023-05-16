package webtoon.main.domains.post.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import webtoon.main.domains.post.service.IPostService;
import webtoon.main.domains.post.entities.dtos.PostDto;

@Controller
@RequestMapping("post")
public class PostController {

    private final IPostService postService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PostController(IPostService postService) {
        super();
        this.postService = postService;
    }


    @GetMapping("{postName}/{id}")
    public String detailPostPage(@PathVariable Long id, Model model) {
        this.logger.info("Entering detail post page with id: {}", id);
        PostDto post = this.postService.findByID(id);
        model.addAttribute("postModel", post);

        PostDto[] prevNextPosts = this.postService.findNextPrevPosts(id, post.getCategory().getId());
        model.addAttribute("prevPost", prevNextPosts[0]);
        model.addAttribute("nextPost", prevNextPosts[1]);

        return "post/detail-post";
    }
    
}
