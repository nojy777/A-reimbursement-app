package com.enyawevia.dto;

import java.util.Date;

public class ResolvedReimbursementReponse {
    private String title;
    private String description;
    private double amount;
    private Date createdAt;
    private String reviewedBy;
    private Date reviewedAt;
    private String status;

    public ResolvedReimbursementReponse() {}

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

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Date getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Date reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ResolvedReimbursementReponse{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                ", reviewedBy='" + reviewedBy + '\'' +
                ", reviewedAt=" + reviewedAt +
                ", status='" + status + '\'' +
                '}';
    }
}
