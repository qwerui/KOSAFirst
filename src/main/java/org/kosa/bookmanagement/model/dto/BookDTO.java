package org.kosa.bookmanagement.model.dto;

public class BookDTO {
	
	private String isbn;
	private String title;
	private String author;
	private String publisher;
	private String publishYear;
	private String category;
	
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPublishYear() {
		return publishYear;
	}
	public void setPublishYear(String publishYear) {
		this.publishYear = publishYear;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}



}
