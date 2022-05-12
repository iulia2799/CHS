package com.example.chs.data.login;

import java.util.HashMap;

public class Primarie implements Login{
    private String primarie;
    private String password;
    private String location;
    private String email;
    private HashMap<String,String> notifications = new HashMap<>();
    private String informatii;
    private String[] links;
    private int points;

    public Primarie(){

    }
    public Primarie(String email, String password){
        this.email =email;
        this.password =password;
        this.informatii="info";
        this.points=0;
    }
    public Primarie(String email, String password, String primarie){
        // if user does not exits in json
        this.primarie=primarie;
        this.password = password;
        this.email = email;
        this.informatii="info";
        this.points=0;
    }

    public Primarie(String email, String primarie, String location, String password){
        this.email = email;
        this.primarie = primarie;
        this.password = password;
        this.location = location;
        this.informatii="info";
        this.points=0;
    }

    public Primarie (String email, String primarie, String password, String location, String informatii){
        this.email = email;
        this.primarie = primarie;
        this.password = password;
        this.location = location;
        this.informatii = informatii;
        this.points=0;
    }

    @Override
    public void SetPassword(String new_password) {
        this.password = new_password;
    }

    @Override
    public void Setusername(String newusername) {
        this.primarie = newusername;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPrimarie() {
        return primarie;
    }

    public HashMap<String, String> getNotifications() {
        return notifications;
    }

    public void setNotifications(HashMap<String, String> notifications) {
        this.notifications = notifications;
    }

    public void setPrimarie(String primarie) {
        this.primarie = primarie;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInformatii() {
        return informatii;
    }

    public void setInformatii(String informatii) {
        this.informatii = informatii;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String[] getLinks() {
        return links;
    }

    public int getPoints() {
        return points;
    }

    public void setLinks(String[] links) {
        this.links = links;
    }
}
