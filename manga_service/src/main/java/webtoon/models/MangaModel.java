package webtoon.models;

import java.util.Date;

import webtoon.enums.EMangaSTS;
import webtoon.enums.EMangaStatus;
import webtoon.enums.EMangaType;

public class MangaModel {
	private Long id;
	private String title;
	private String alternativeTitle;
	private String concerpt;
	private String description;
	private String mangaName;
	private String featuredImage ;
	private EMangaStatus status;
	private EMangaSTS mangaSts;
	private Integer commentCount;
	private EMangaType mangaType;
	private Date created_at;
	private Date modifed_at;
	private Integer rating;
	private Integer view_Count;
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
	public Integer getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
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
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public Integer getView_Count() {
		return view_Count;
	}
	public void setView_Count(Integer view_Count) {
		this.view_Count = view_Count;
	}
	
}
