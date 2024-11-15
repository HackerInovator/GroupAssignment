package com.example.wings2;

public class ReportEntry {
    private String lecturer;
    private String className;
    private String courseName;
    private String chapterCovered;
    private String modeOfDelivery;
    private int missedClasses;
    private int studentAttendance;
    private int studentRegistrations;
    private String challenges;
    private String recommendations;
    private String malpractice;

    public ReportEntry(String lecturer, String className, String courseName, String chapterCovered,
                       String modeOfDelivery, int missedClasses, int studentAttendance,
                       int studentRegistrations, String challenges, String recommendations,
                       String malpractice) {
        this.lecturer = lecturer;
        this.className = className;
        this.courseName = courseName;
        this.chapterCovered = chapterCovered;
        this.modeOfDelivery = modeOfDelivery;
        this.missedClasses = missedClasses;
        this.studentAttendance = studentAttendance;
        this.studentRegistrations = studentRegistrations;
        this.challenges = challenges;
        this.recommendations = recommendations;
        this.malpractice = malpractice;
    }

    // Getters for each field
    public String getLecturer() { return lecturer; }
    public String getClassName() { return className; }
    public String getCourseName() { return courseName; }
    public String getChapterCovered() { return chapterCovered; }
    public String getModeOfDelivery() { return modeOfDelivery; }
    public int getMissedClasses() { return missedClasses; }
    public int getStudentAttendance() { return studentAttendance; }
    public int getStudentRegistrations() { return studentRegistrations; }
    public String getChallenges() { return challenges; }
    public String getRecommendations() { return recommendations; }
    public String getMalpractice() { return malpractice; }
}