package webtoon.domains.tag.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITagRelationRepository
		extends JpaRepository<TagEntityRelation, Long>, JpaSpecificationExecutor<TagEntityRelation> {

	List<TagEntityRelation> findAllByObjectIDAndTagType(Long objectID, String type);

	@Modifying
	void deleteAllByObjectIDAndTagType(Long objectID, String type);

	TagEntityRelation findByObjectIDAndTagType(Long id, String post);
}
