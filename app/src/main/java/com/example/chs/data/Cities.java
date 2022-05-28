package com.example.chs.data;

public enum Cities {
    ARAD("Arad"),
    TIMISOARA("Timișoara"),
    RESITA("Reșița"),
    CARANSEBES("Caransebeș"),
    MONIOM("Moniom");

    private String name;
    private Cities(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
