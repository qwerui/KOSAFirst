package org.kosa.bookmanagement.model.dto;

public class MemberDTO {
    private String id;
    private String password;
    private String name;
    private String phone;
    private int typeNumber;

    public MemberDTO() {
    }

    public MemberDTO(String id, String password, String name, String phone) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public MemberDTO(String id, String password, String name, String phone, int typeNumber) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.typeNumber = typeNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(int typeNumber) {this.typeNumber = typeNumber;}


}
