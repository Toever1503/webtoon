package webtoon.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

import webtoon.enums.EMangaSTS;
import webtoon.enums.EMangaStatus;
import webtoon.enums.EMangaType;

@Entity
@Table
public class MangaEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private Long id;

@Column(name = "title")
private String title;

@Column(name = "alternative_title")
private String alternativeTitle;

@Column(name = "concerpt")
private String concerpt;

@Column(name = "description")
private String description;

@Column(name = "manga_name")
private String mangaName;

@Column(name = "featured_image")
private String featuredImage ;

@JoinColumn(name = "status")
private EMangaStatus status;

@JoinColumn(name = "manga_sts")
private EMangaSTS mangaSts;

@Column(name = "comment_count")
private int commentCount;

@JoinColumn(name = "manga_type")
private EMangaType mangaType;

@Column
@Temporal(TemporalType.TIMESTAMP)
@UpdateTimestamp
private Date created_at;

@Column
@Temporal(TemporalType.TIMESTAMP)
@UpdateTimestamp
private Date modifed_at;

@Column(name = "rating")
private int rating;

@Column(name = "view_count")
private int view_Count;

@Column
@Temporal(TemporalType.TIMESTAMP)
@UpdateTimestamp
private Date published_date;

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getAlternativeTitle() {
	return alternativeTitle;
}

public void setAlternativeTitle(String alternativeTitle) {
	this.alternativeTitle = alternativeTitle;
}

public String getConcerpt() {
	return concerpt;
}

public void setConcerpt(String concerpt) {
	this.concerpt = concerpt;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public String getMangaName() {
	return mangaName;
}

public void setMangaName(String mangaName) {
	this.mangaName = mangaName;
}

public String getFeaturedImage() {
	return featuredImage;
}

public void setFeaturedImage(String featuredImage) {
	this.featuredImage = featuredImage;
}

public EMangaStatus getStatus() {
	return status;
}

public void setStatus(EMangaStatus status) {
	this.status = status;
}

public EMangaSTS getMangaSts() {
	return mangaSts;
}

public void setMangaSts(EMangaSTS mangaSts) {
	this.mangaSts = mangaSts;
}

public int getCommentCount() {
	return commentCount;
}

public void setCommentCount(int commentCount) {
	this.commentCount = commentCount;
}

public EMangaType getMangaType() {
	return mangaType;
}

public void setMangaType(EMangaType mangaType) {
	this.mangaType = mangaType;
}

public Date getCreated_at() {
	return created_at;
}

public void setCreated_at(Date created_at) {
	this.created_at = created_at;
}

public Date getModifed_at() {
	return modifed_at;
}

public void setModifed_at(Date modifed_at) {
	this.modifed_at = modifed_at;
}

public int getRating() {
	return rating;
}

public void setRating(int rating) {
	this.rating = rating;
}

public int getView_Count() {
	return view_Count;
}

public void setView_Count(int view_Count) {
	this.view_Count = view_Count;
}

public Date getPublished_date() {
	return published_date;
}

public void setPublished_date(Date published_date) {
	this.published_date = published_date;
}

//private  created_by;
//
//private modified_by;

}
