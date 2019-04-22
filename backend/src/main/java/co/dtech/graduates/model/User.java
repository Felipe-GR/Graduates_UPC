package co.dtech.graduates.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private String profilePicture;
    private String city;
    private String profession;
    private String company;
    private String education;
    private Byte publicPhoneNumber;
    private Byte publicCity;
    private Byte publicProfession;
    private Byte publicCompany;
    private Byte publicEducation;
    private Byte isAdmin;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "profile_picture")
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Basic
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "profession")
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Basic
    @Column(name = "company")
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Basic
    @Column(name = "education")
    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    @Basic
    @Column(name = "public_phone_number")
    public Byte getPublicPhoneNumber() {
        return publicPhoneNumber;
    }

    public void setPublicPhoneNumber(Byte publicPhoneNumber) {
        this.publicPhoneNumber = publicPhoneNumber;
    }

    @Basic
    @Column(name = "public_city")
    public Byte getPublicCity() {
        return publicCity;
    }

    public void setPublicCity(Byte publicCity) {
        this.publicCity = publicCity;
    }

    @Basic
    @Column(name = "public_profession")
    public Byte getPublicProfession() {
        return publicProfession;
    }

    public void setPublicProfession(Byte publicProfession) {
        this.publicProfession = publicProfession;
    }

    @Basic
    @Column(name = "public_company")
    public Byte getPublicCompany() {
        return publicCompany;
    }

    public void setPublicCompany(Byte publicCompany) {
        this.publicCompany = publicCompany;
    }

    @Basic
    @Column(name = "public_education")
    public Byte getPublicEducation() {
        return publicEducation;
    }

    public void setPublicEducation(Byte publicEducation) {
        this.publicEducation = publicEducation;
    }

    @Basic
    @Column(name = "is_admin")
    public Byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(profilePicture, user.profilePicture) &&
                Objects.equals(city, user.city) &&
                Objects.equals(profession, user.profession) &&
                Objects.equals(company, user.company) &&
                Objects.equals(education, user.education) &&
                Objects.equals(publicPhoneNumber, user.publicPhoneNumber) &&
                Objects.equals(publicCity, user.publicCity) &&
                Objects.equals(publicProfession, user.publicProfession) &&
                Objects.equals(publicCompany, user.publicCompany) &&
                Objects.equals(publicEducation, user.publicEducation) &&
                Objects.equals(isAdmin, user.isAdmin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, surname, phoneNumber, profilePicture, city, profession, company, education, publicPhoneNumber, publicCity, publicProfession, publicCompany, publicEducation, isAdmin);
    }
}
