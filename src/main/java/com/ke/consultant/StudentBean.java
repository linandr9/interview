package com.ke.consultant;

import java.util.Objects;

/**
 * DAO for student database
 */
public class StudentBean {
    private Integer id;
    private String first;
    private String last;
    private String mail;

    @Override
    public String toString() {
        return "StudentBean{" +
                "id=" + id +
                ", first='" + first + '\'' +
                ", last='" + last + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentBean that = (StudentBean) o;
        return id.equals(that.id) &&
                Objects.equals(first, that.first) &&
                Objects.equals(last, that.last) &&
                Objects.equals(mail, that.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, first, last, mail);
    }
}
