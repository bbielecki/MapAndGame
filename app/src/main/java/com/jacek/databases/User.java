package com.jacek.databases;

import java.util.ArrayList;

/**
 * Created by Jacek on 2017-04-27.
 */

public class User {

    private int id;
    private String name;
    private String surname;
    private double points;
    private int age;
    private int weight;
    private int height;
    private int level;

    public User(){

    }

    public User(String mName, String mSurname, double mPoints, int mAge, int mWeight, int mHeight, int mLevel){
        name = mName;
        surname = mSurname;
        points = mPoints;
        age = mAge;
        weight = mWeight;
        height = mHeight;
        level = mLevel;

    }

    public static ArrayList<User> makeUsers(){
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Jacek", "Polak", 100000, 22 , 82, 182, 90));
        users.add(new User("Jakub", "Jaszczuk", 100000, 21 , -20, 182, 90));
        users.add(new User("Bartłomiej", "Bielecki", 100000, 22 , 76, -30, 90));
        return users;
    }



    //region GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
//endregion


}
