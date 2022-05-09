package com.example.chs.data;



import com.example.chs.data.login.Primarie;
import com.example.chs.data.login.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
    private String name;
    private String location;
    private String description;
    private String image;
    private String status;
    private User op;
    private Categorie cat;
    private static Integer trackingId =100;
    private Date timerDate;
    private Primarie assignee;
    private int spam=0;
    private List<String> report= new ArrayList<>();
    public Post(){}
    public Post(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.status = "posted";
        this.cat = new Categorie("test");
        trackingId++;

    }
    public Post(String name, String location, String description,Categorie categorie) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.status = "posted";
        this.cat = categorie;
        trackingId++;

    }
    public Post(String name,String location,String description,User user,Categorie cat){
        this.name = name;
        this.location = location;
        this.description = description;
        this.op = user;
        this.op.SetPassword("********");
        this.status = "posted";
        this.cat = cat;
        trackingId++;

    }
    public Post(String name,String location,String description,User user,Categorie cat,String received){
        this.name = name;
        this.location = location;
        this.description = description;
        this.op = user;
        this.op.SetPassword("********");
        this.status = "posted";
        this.cat = cat;
        this.image = received;
        trackingId++;

    }
    public String getName(){
        return this.name;
    }
    public String getLocation(){
        return this.location;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImages(String list){
        this.image=list;
    }

    public String getImages() {
        return image;
    }

    public User getOp() {
        if(op==null) return null;
        return op;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }

    public static Integer getTrackingId() {
        return trackingId;
    }
    public static void setTrackingId(Integer trackingid1){
        trackingId = trackingid1;}
    public String getCategorie(){
        return cat.getNume();
    }

    public void setCat(Categorie cat) {
        this.cat = cat;
    }

    public void setOp(User op) {
        this.op = op;
    }

    public Date getTimerDate() {
        return timerDate;
    }

    public Primarie getAssignee() {
        return assignee;
    }

    public void setTimerDate(Date timerDate) {
        this.timerDate = timerDate;
    }

    public void setAssignee(Primarie assignee) {
        this.assignee = assignee;
    }
    public int getSpam() {
        return spam;
    }
    public void setSpam(int spam) {
        this.spam = spam;
    }

    public List<String> getReport() {
        return report;
    }

    public void setReport(List<String> report) {
        this.report = report;
    }
}
