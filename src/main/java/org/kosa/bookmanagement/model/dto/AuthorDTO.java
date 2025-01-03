package org.kosa.bookmanagement.model.dto;

public class AuthorDTO {
    private int authorNumber;
    private String authorName;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getAuthorNumber() {
        return authorNumber;
    }

    public void setAuthorNumber(int authorNumber) {
        this.authorNumber = authorNumber;
    }
}
