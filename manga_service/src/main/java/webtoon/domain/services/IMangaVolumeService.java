package webtoon.domain.services;

import webtoon.domain.dtos.MangaVolumeDto;
import webtoon.domain.models.MangaVolumeModel;

public interface IMangaVolumeService {

	boolean deleteById(Long id);

	MangaVolumeDto update(MangaVolumeModel model);

	MangaVolumeDto add(MangaVolumeModel model);

}
