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
    private String trackingnumber;
    private Date timerDate;
    private long datet;
    private Primarie assignee;
    private int spam=0;
    private int voturi=0;
    private List<String> report= new ArrayList<>();
    public Post(){}
    public Post(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.status = "posted";
        this.cat = new Categorie("test");

        this.assignee = null;

    }
    public Post(String name, String location, String description,Categorie categorie) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.status = "posted";
        this.cat = categorie;

        this.assignee = null;


    }
    public Post(String name,String location,String description,User user,Categorie cat){
        this.name = name;
        this.location = location;
        this.description = description;
        this.op = user;
        this.op.SetPassword("********");
        this.status = "posted";
        this.cat = cat;

        this.assignee = null;


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

        this.assignee = null;


    }

    public Post(String name,String location,String description,User user,Categorie cat,String received,long date){
        this.name = name;
        this.location = location;
        this.description = description;
        this.op = user;
        this.datet=date;
        this.op.SetPassword("********");
        this.status = "posted";
        this.cat = cat;
        this.image = received;

        this.assignee = null;


    }

    public Post(String name, String strAdd, String description, Categorie categorie, long date) {
        this.name = name;
        this.location = strAdd;
        this.description = description;
        this.cat= categorie;
        this.datet = date;
        this.timerDate = timerDate;
        //this.op.SetPassword("********");
        this.status = "posted";
        this.cat = cat;
        this.assignee = null;

    }

    public Post(String name, String strAdd, String description, User user, Categorie cat, long time) {
        this.name = name;
        this.location = strAdd;
        this.description = description;
        this.op = user;
        this.datet = time;
        this.op.SetPassword("********");
        this.status = "posted";
        this.cat = cat;
        this.assignee = null;


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

    public int getVoturi() {
        return voturi;
    }

    public void setVoturi(int voturi) {
        this.voturi = voturi;
    }

    public long getDatet() {
        return datet;
    }

    public void setDatet(long datet) {
        this.datet = datet;
    }

    public String getTrackingnumber() {
        return trackingnumber;
    }

    public void setTrackingnumber(String trackingnumber) {
        this.trackingnumber = trackingnumber;
    }
}
