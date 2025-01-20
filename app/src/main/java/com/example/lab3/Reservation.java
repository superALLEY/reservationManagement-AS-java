package com.example.lab3;

public class Reservation {
    private long id;
    private String name;
    private String email;
    private int peopleCount;
    private String startDate;
    private String endDate;
    private int totalDays;
    private int totalPrice;
    private String services;

    public Reservation(long id, String name, String email, int peopleCount, String startDate, String endDate, int totalDays, int totalPrice, String services) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.peopleCount = peopleCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays;
        this.totalPrice = totalPrice;
        this.services = services;
    }

    // Getters for all fields
    public long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getPeopleCount() { return peopleCount; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public int getTotalDays() { return totalDays; }
    public int getTotalPrice() { return totalPrice; }
    public String getServices() { return services; }
}

