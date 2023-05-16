package webtoon.main.storage.infras.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class PageableBean {

	private Pageable pageable;

	public PageableBean(HttpServletRequest req) {
		System.out.println("creating PageableBean page: " + req.getParameter("page"));
		System.out.println("creating PageableBean size: " + req.getParameter("size"));
		System.out.println("creating PageableBean sort: " + req.getParameter("sort"));
		String pg = req.getParameter("page");
		String sz = req.getParameter("size");
		String sort = req.getParameter("sort");
		int page = 0, size = 20;
		try {
			if (pg != null) {
				page = Integer.valueOf(pg);
			}
			if (sz != null)
				size = Integer.valueOf(sz);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("page or size must be number");
		}
		List<Order> colOrders = new ArrayList<Order>();

		if (page < 0)
			page = 0;

		if (size < 0)
			size = 20;

		if (sort != null) {
			if (!sort.isBlank()) {
				String[] sortList = sort.split(";");
				for (String item : sortList) {
					String[] sortCol = item.split(",");
					if (sortCol.length != 2)
						throw new RuntimeException("Sort param is invalid");
					try {
						colOrders.add(new Order(Sort.Direction.fromString(sortCol[1]), sortCol[0]));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						throw new RuntimeException(
								"Sort direction is invalid: column: " + sortCol[0] + ", direction: " + sortCol[1]);
					}
				}
			}
		}
		this.pageable = PageRequest.of(page, size, Sort.by(colOrders));
	}

	public Pageable getPageable() {
		// TODO Auto-generated method stub
		return this.pageable;
	}

}
