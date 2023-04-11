package webtoon.domains.tag.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITagRelationRepository
		extends JpaRepository<TagEntityRelation, Long>, JpaSpecificationExecutor<TagEntityRelation> {

	List<TagEntityRelation> findAllByObjectIDAndTagType(Long objectID, String type);

	@Modifying
	@Query("delete from TagEntityRelation t where t.objectID = ?1 and t.tagType = ?2")
	void deleteAllByObjectIDAndTagType(Long objectID, String type);

	TagEntityRelation findByObjectIDAndTagType(Long id, String post);
}
