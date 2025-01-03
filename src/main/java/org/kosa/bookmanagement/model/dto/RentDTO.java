package org.kosa.bookmanagement.model.dto;

import java.sql.Date;

public class RentDTO {
	private int rentNumber;
	private Date rentDate;
	private Date returnDate; // Nullable
	private int extended;
	private String id;
	private int bookNumber;
	private String isbn;
	private String title;
	private String authors;

	public RentDTO(){
	}

	public RentDTO(int rentNumber, Date rentDate, Date returnDate, int extended, String id, int bookNumber, String isbn) {
		this.rentNumber = rentNumber;
		this.rentDate = rentDate;
		this.returnDate = returnDate;
		this.extended = extended;
		this.id = id;
		this.bookNumber = bookNumber;
		this.isbn = isbn;
	}

	public RentDTO(Date rentDate, Date returnDate, int extended, String id, int bookNumber, String isbn) {
		this.rentDate = rentDate;
		this.returnDate = returnDate;
		this.extended = extended;
		this.id = id;
		this.bookNumber = bookNumber;
		this.isbn = isbn;
	}

	public RentDTO(int rentNumber, int bookNumber, String isbn, String title, String authors, Date rentDate, Date returnDate, int extended) {
		this.rentNumber = rentNumber;
		this.bookNumber = bookNumber;
		this.isbn = isbn;
		this.title = title;
		this.authors = authors;
		this.rentDate = rentDate;
		this.returnDate = returnDate;
		this.extended = extended;
	}

	public RentDTO(int rentNumber, String id, int bookNumber, String isbn, String title, String authors, Date rentDate, Date returnDate, int extended) {
		this.rentNumber = rentNumber;
		this.id = id;
		this.bookNumber = bookNumber;
		this.isbn = isbn;
		this.title = title;
		this.authors = authors;
		this.rentDate = rentDate;
		this.returnDate = returnDate;
		this.extended = extended;
	}

	public int getRentNumber() {
		return rentNumber;
	}

	public void setRentNumber(int rentNumber) {
		this.rentNumber = rentNumber;
	}

	public Date getRentDate() {
		return rentDate;
	}

	public void setRentDate(Date rentDate) {
		this.rentDate = rentDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public int getExtended() {
		return extended;
	}

	public void setExtended(int extended) {
		this.extended = extended;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getBookNumber() {
		return bookNumber;
	}

	public void setBookNumber(int bookNumber) {
		this.bookNumber = bookNumber;
	}

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

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	@Override
	public String toString() {
		return "RentDTO{" +
				"rentNumber=" + rentNumber +
				", rentDate=" + rentDate +
				", returnDate=" + returnDate +
				", extended='" + extended + '\'' +
				", id='" + id + '\'' +
				", bookNumber=" + bookNumber +
				", isbn='" + isbn + '\'' +
				'}';
	}
}
