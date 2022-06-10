package com.example.chs.data.model;

public class Alert {
    private String date;
    private String description;
    private String link = "";

    public Alert(String date, String desc){
        this.date=date;
        this.description=desc;
    }
    public Alert(String date, String desc,String link){
        this.date=date;
        this.description=desc;
        this.link = link;
    }
    public Alert(){

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
