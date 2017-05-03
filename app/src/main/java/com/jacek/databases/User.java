package com.jacek.databases;

import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jacek on 2017-04-27.
 */

public class User {

    private int id;
    private String name;
    private String password;
    private String surname;
    private double points;
    private int age;
    private int weight;
    private int height;
    private int level;

    public User(){

    }

    public User(String mName, String mPassword, String mSurname, double mPoints, int mAge, int mWeight, int mHeight, int mLevel){
        name = mName;
        password = mPassword;
        surname = mSurname;
        points = mPoints;
        age = mAge;
        weight = mWeight;
        height = mHeight;
        level = mLevel;

    }

    public static ArrayList<User> makeUsers(){
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Jacek", MD5Hasher.hashWithMD5("elo"), "Polak", 100000, 22 , 82, 182, 90));
        users.add(new User("Jakub", "elo2o","Jaszczuk", 100000, 21 , -20, 182, 90));
        users.add(new User("Bartłomiej", "elooo", "Bielecki", 100000, 22 , 76, -30, 90));
        return users;
    }



    //region GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
