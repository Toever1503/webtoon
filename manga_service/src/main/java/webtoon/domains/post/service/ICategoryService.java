package webtoon.domains.post.service;

import webtoon.domains.post.entities.CategoryEntity;

import java.util.List;

public interface ICategoryService {
    List<CategoryEntity> findAllCate();
}
