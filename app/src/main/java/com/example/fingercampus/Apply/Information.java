package com.example.fingercampus.Apply;

public class Information {
    private String id,name,phone,classroom,date,users,section;
    public Information(String iden,String n,String p,String c,String d,String u,String s){
        id = iden;
        name = n;
        phone = p;
        classroom = c;
        date = d;
        users = u;
        section = s;
    }

    public String getClassroom() {
        return classroom;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSection() {
        return section;
    }

    public String getUsers() {
        return users;
    }
}
