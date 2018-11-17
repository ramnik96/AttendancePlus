package com.example.ramnik_singh.attendanceplus_student;

public class User{
    public String id;
    public String Name;
    public String SID;
    public String Branch;
    public String Semester;

    public User() {
    }



    public User(String uid, String Name, String SID, String Branch, String Semester){
        this.id=uid;
        this.Name = Name;
        this.SID=SID;
        this.Branch=Branch;
        this.Semester=Semester;


    }
}
