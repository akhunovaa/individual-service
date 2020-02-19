package com.botmasterzzz.individual.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Size;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndividualDTO extends AbstractDto {

    @Size(min = 3, max = 5000)
    private String name;

    @Size(min = 3, max = 5000)
    private String surname;

    @Size(min = 3, max = 5000)
    private String patrName;

    @Size(min = 3, max = 5000)
    private String nickName;

    @Size(min = 3, max = 100)
    private String phone;

    @JsonFormat(pattern="dd.MM.yyyy")
    private Date birthDate;

    @Size(min = 3, max = 10)
    private String gender;

    @Size(min = 3, max = 100)
    private String language;

    @Size(min = 3, max = 100)
    private String city;

    @Size(min = 3, max = 2000)
    private String info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatrName() {
        return patrName;
    }

    public void setPatrName(String patrName) {
        this.patrName = patrName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "IndividualDTO{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patrName='" + patrName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", language='" + language + '\'' +
                ", city='" + city + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}