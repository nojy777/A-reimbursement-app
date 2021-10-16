package com.enyawevia.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class Reimbursement {
    private int id;
    private int userId;
    private String title;
    private String description;
    private double amount;
    private Date createdAt;
    private String status;
    private String reviewedBy;

    public Reimbursement(){

    }


    public Reimbursement(int userId, String title, String description, double amount, Date createdAt, String status, String reviewedBy) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.createdAt = createdAt;
        this.status = status;
        this.reviewedBy = reviewedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
}
