package com.my.vibras.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ravindra Birla on 16,August,2022
 */
public class SuccessResGetSubscription implements Serializable {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Result implements Serializable{

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("total_like")
        @Expose
        public String totalLike;
        @SerializedName("for_month")
        @Expose
        public String forMonth;
        @SerializedName("total_group")
        @Expose
        public String totalGroup;
        @SerializedName("plan_type")
        @Expose
        public String planType;
        @SerializedName("monthly_price")
        @Expose
        public String monthlyPrice;
        @SerializedName("half_yearly_price")
        @Expose
        public String halfYearlyPrice;
        @SerializedName("yearly_plan_price")
        @Expose
        public String yearlyPlanPrice;
        @SerializedName("sort_order")
        @Expose
        public String sortOrder;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTotalLike() {
            return totalLike;
        }

        public void setTotalLike(String totalLike) {
            this.totalLike = totalLike;
        }

        public String getForMonth() {
            return forMonth;
        }

        public void setForMonth(String forMonth) {
            this.forMonth = forMonth;
        }

        public String getTotalGroup() {
            return totalGroup;
        }

        public void setTotalGroup(String totalGroup) {
            this.totalGroup = totalGroup;
        }

        public String getPlanType() {
            return planType;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
        }

        public String getMonthlyPrice() {
            return monthlyPrice;
        }

        public void setMonthlyPrice(String monthlyPrice) {
            this.monthlyPrice = monthlyPrice;
        }

        public String getHalfYearlyPrice() {
            return halfYearlyPrice;
        }

        public void setHalfYearlyPrice(String halfYearlyPrice) {
            this.halfYearlyPrice = halfYearlyPrice;
        }

        public String getYearlyPlanPrice() {
            return yearlyPlanPrice;
        }

        public void setYearlyPlanPrice(String yearlyPlanPrice) {
            this.yearlyPlanPrice = yearlyPlanPrice;
        }

        public String getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }


}
