package webtoon.storage.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("queries")
public class QueryResource {

	public String filter() {
		return "filtered";
	}
}
