package com.botmasterzzz.individual.entity;

import com.botmasterzzz.individual.entity.api.ApiDataEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_bookmark")
public class UserBookmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = CascadeType.REFRESH)
    private User user;

    @Column(name = "note")
    private String note;

    @JoinColumn(name = "api_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private ApiDataEntity apiDataEntity;

    @JsonIgnore
    @Column(name = "aud_when_create")
    private Timestamp audWhenCreate;

    @JsonIgnore
    @Column(name = "aud_when_update")
    private Timestamp audWhenUpdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ApiDataEntity getApiDataEntity() {
        return apiDataEntity;
    }

    public void setApiDataEntity(ApiDataEntity apiDataEntity) {
        this.apiDataEntity = apiDataEntity;
    }

    public Timestamp getAudWhenCreate() {
        return audWhenCreate;
    }

    public void setAudWhenCreate(Timestamp audWhenCreate) {
        this.audWhenCreate = audWhenCreate;
    }

    public Timestamp getAudWhenUpdate() {
        return audWhenUpdate;
    }

    public void setAudWhenUpdate(Timestamp audWhenUpdate) {
        this.audWhenUpdate = audWhenUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBookmarkEntity that = (UserBookmarkEntity) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(name, that.name) &&
                Objects.equal(fullName, that.fullName) &&
                Objects.equal(user, that.user) &&
                Objects.equal(note, that.note) &&
                Objects.equal(apiDataEntity, that.apiDataEntity) &&
                Objects.equal(audWhenCreate, that.audWhenCreate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, fullName, user, note, apiDataEntity, audWhenCreate);
    }

    @Override
    public String toString() {
        return "UserBookmarkEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", user=" + user +
                ", note='" + note + '\'' +
                ", apiDataEntity=" + apiDataEntity +
                ", audWhenCreate=" + audWhenCreate +
                ", audWhenUpdate=" + audWhenUpdate +
                '}';
    }
}
