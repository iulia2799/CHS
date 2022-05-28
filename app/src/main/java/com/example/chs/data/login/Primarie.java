package com.example.chs.data.login;

import com.example.chs.data.model.Act;
import com.example.chs.data.model.Alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Primarie implements Login{
    private String primarie;
    private String password;
    private String location;
    private String email;
    private List<Alert> alertList = new ArrayList<>();
    private String informatii;
    private List<Act> links = new ArrayList<>();
    private int points;

    public Primarie(){

    }
    public Primarie(String email, String password){
        this.email =email;
        this.password =password;
        this.informatii="info";
        this.points=0;
    }
    public Primarie(String email, String primarie, String password){
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
    public Primarie (String email, String primarie, String password, String location, String informatii,List<Act> links){
        this.email = email;
        this.primarie = primarie;
        this.password = password;
        this.location = location;
        this.informatii = informatii;
        this.points=0;
        this.links.addAll(links);
    }
    public Primarie (String email, String primarie, String password, String location, String informatii,List<Act> links,List<Alert> alist){
        this.email = email;
        this.primarie = primarie;
        this.password = password;
        this.location = location;
        this.informatii = informatii;
        this.points=0;
        this.links.addAll(links);
        this.alertList.addAll(alist);
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

    public List<Alert> getAlertList() {
        return alertList;
    }

    public void setAlertList(List<Alert> alertList) {
        for(Alert a : alertList) {
            this.alertList.add(a);
        }
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

    public int getPoints() {
        return points;
    }

    public void setLinks(List<Act> links) {
        this.links = links;
    }

    public List<Act> getLinks() {
        return links;
    }
}
