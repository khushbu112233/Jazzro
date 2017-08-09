package com.jlouistechnology.Jazzro.Model;

import java.util.ArrayList;

/**
 * Created by aipxperts on 9/12/16.
 */
public class Contact {

    public String id;
    public String fname;
    public String lname;
    public String streetaddress;
    public String city;
    public String state;
    public String zipcode;
    public String note;
    public String image;
    public String image_url;
    public String phone1;
    public String phone2;
    public String phone3;
    public String email1;
    public String email2;
    public String email3;
    public String company_name;
    public String company_title;
    public String uniqueId;
    public String birthday;
    public String work_anniversary;
    public String created_at_formatted;
    public String updated_at_formatted;
    public ArrayList<Group> group_list;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getStreetaddress() {
        return streetaddress;
    }

    public void setStreetaddress(String streetaddress) {
        this.streetaddress = streetaddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_title() {
        return company_title;
    }

    public void setCompany_title(String company_title) {
        this.company_title = company_title;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getWork_anniversary() {
        return work_anniversary;
    }

    public void setWork_anniversary(String work_anniversary) {
        this.work_anniversary = work_anniversary;
    }

    public String getCreated_at_formatted() {
        return created_at_formatted;
    }

    public void setCreated_at_formatted(String created_at_formatted) {
        this.created_at_formatted = created_at_formatted;
    }

    public String getUpdated_at_formatted() {
        return updated_at_formatted;
    }

    public void setUpdated_at_formatted(String updated_at_formatted) {
        this.updated_at_formatted = updated_at_formatted;
    }

    public ArrayList<Group> getGroup_list() {
        return group_list;
    }

    public void setGroup_list(ArrayList<Group> group_list) {
        this.group_list = group_list;
    }
}
