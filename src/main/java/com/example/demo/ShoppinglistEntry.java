package com.example.demo;

public class ShoppinglistEntry {
    private String name;
    private boolean done;


    public ShoppinglistEntry(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {return done;}

    public void setDone(boolean done) {this.done = done;}
}