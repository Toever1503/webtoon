package webtoon.domains.post.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import webtoon.domains.post.entities.CategoryEntity;
import webtoon.domains.post.entities.ICategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService{
    private final ICategoryRepository categoryRepository;

    public CategoryServiceImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryEntity> findAllCate(){

        return this.categoryRepository.findAll()
                .stream().map(categoryEntity -> {
                    return CategoryEntity.builder()
                            .id(categoryEntity.getId())
                            .categoryName(categoryEntity.getCategoryName())
                            .slug(categoryEntity.getSlug())
                            .build();
                }).collect(Collectors.toList());
    }

}
