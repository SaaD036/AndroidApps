package com.example.coachingmanagement;

public class Batch {
    private String name;
    private int totalStudent;

    Batch(){
        totalStudent = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalStudent() {
        return totalStudent;
    }

    public void setTotalStudent(int totalStudent) {
        this.totalStudent = totalStudent;
    }
}
