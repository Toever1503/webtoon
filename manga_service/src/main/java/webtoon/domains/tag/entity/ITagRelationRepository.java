package webtoon.domains.tag.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ITagRelationRepository
		extends JpaRepository<TagEntityRelation, Long>, JpaSpecificationExecutor<TagEntityRelation> {

	TagEntityRelation findByObjectIDAndTagType(Long objectID, String type);
}
