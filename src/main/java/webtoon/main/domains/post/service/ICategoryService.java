package webtoon.main.domains.post.service;

import webtoon.main.domains.post.entities.CategoryEntity;

import java.util.List;

public interface ICategoryService {
    List<CategoryEntity> findAllCate();
}
