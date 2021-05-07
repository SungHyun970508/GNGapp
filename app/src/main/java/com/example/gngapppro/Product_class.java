package com.example.gngapppro;

public class Product_class {
    private int proIDX;
    private String proName;
    private int proCnt;
    private int proCost;
    private int proSection;
    private int proWeight;

    public Product_class(){}

    public int getProIDX() {
        return proIDX;
    }

    public void setProIDX(int proIDX) {
        this.proIDX = proIDX;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public int getProCost() {
        return proCost;
    }

    public void setProCost(int proCost) {
        this.proCost = proCost;
    }

    public int getProCnt() {
        return proCnt;
    }

    public void setProCnt(int proCnt) {
        this.proCnt = proCnt;
    }

    public int getProSection() {
        return proSection;
    }

    public void setProSection(int proSection) {
        this.proSection = proSection;
    }

    public int getProWeight() {
        return proWeight;
    }

    public void setProWeight(int proWeight) {
        this.proWeight = proWeight;
    }
}
