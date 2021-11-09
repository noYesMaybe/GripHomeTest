package com.example.griphometest.model;

public class Stat {

    private int baseStat;

    private String name;

    public Stat(int baseStat, String name) {
        this.baseStat = baseStat;
        this.name = name;
    }

    public int getBaseStat() {
        return baseStat;
    }

    public String getName() {
        return name;
    }
}
