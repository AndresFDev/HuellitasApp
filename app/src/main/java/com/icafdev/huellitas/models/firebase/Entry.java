package com.icafdev.huellitas.models.firebase;

public class Entry {

    private String id;
    private String userId;
    private String userPhoto;
    private String userName;
    private String photo;
    private String status;
    private String date;
    private String city;
    private String address;
    private String pet_name;
    private String type;
    private String raza;
    private String gen;
    private String description;
    private String name;
    private String phone;
    private long time;

    public Entry() {
    }

    public Entry(String id, String userId, String userPhoto, String userName, String photo, String status, String date, String city, String address, String pet_name, String type, String raza, String gen, String description, String name, String phone, long time) {
        this.id = id;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.userName = userName;
        this.photo = photo;
        this.status = status;
        this.date = date;
        this.city = city;
        this.address = address;
        this.pet_name = pet_name;
        this.type = type;
        this.raza = raza;
        this.gen = gen;
        this.description = description;
        this.name = name;
        this.phone = phone;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
