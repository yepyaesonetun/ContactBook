package com.prime.awitd.contactbook.model;

/**
 * Created by SantaClaus on 06/12/2016.
 */

public class Member {
    private String name;
    private String team;
    private String id;
    private int phone;
    private int home;
    private String imgUrl;

    public Member() {
    }

    public Member(String name, String team, String id, int phone, int home, String imgUrl) {
        this.name = name;
        this.team = team;
        this.id = id;
        this.phone = phone;
        this.home = home;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getHome() {
        return home;
    }

    public void setHome(int home) {
        this.home = home;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
