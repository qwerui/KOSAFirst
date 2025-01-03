package org.kosa.bookmanagement.model.dto;

public class CopyDTO {
	private String BookNumber;
	private String isbn;
	private String position;
	
	public String getBookNumber() {
		return BookNumber;
	}
	public void setBookNumber(String bookNumber) {
		BookNumber = bookNumber;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
}
