package com.my.vibras.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ravindra Birla on 16,August,2022
 */
public class SuccessResAddCard implements Serializable {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("card_name")
        @Expose
        public String cardName;
        @SerializedName("card_num")
        @Expose
        public String cardNum;
        @SerializedName("card_month")
        @Expose
        public String cardMonth;
        @SerializedName("card_exp")
        @Expose
        public String cardExp;
        @SerializedName("card_cvv")
        @Expose
        public String cardCvv;
        @SerializedName("date_time")
        @Expose
        public String dateTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public String getCardNum() {
            return cardNum;
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }

        public String getCardMonth() {
            return cardMonth;
        }

        public void setCardMonth(String cardMonth) {
            this.cardMonth = cardMonth;
        }

        public String getCardExp() {
            return cardExp;
        }

        public void setCardExp(String cardExp) {
            this.cardExp = cardExp;
        }

        public String getCardCvv() {
            return cardCvv;
        }

        public void setCardCvv(String cardCvv) {
            this.cardCvv = cardCvv;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }

}
