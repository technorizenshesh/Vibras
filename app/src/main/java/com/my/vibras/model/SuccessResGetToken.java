package com.my.vibras.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessResGetToken {


    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("message")
    @Expose
    public String message;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Metadata {

    }

    public class Card {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("object")
        @Expose
        public String object;
        @SerializedName("address_city")
        @Expose
        public Object addressCity;
        @SerializedName("address_country")
        @Expose
        public Object addressCountry;
        @SerializedName("address_line1")
        @Expose
        public Object addressLine1;
        @SerializedName("address_line1_check")
        @Expose
        public Object addressLine1Check;
        @SerializedName("address_line2")
        @Expose
        public Object addressLine2;
        @SerializedName("address_state")
        @Expose
        public Object addressState;
        @SerializedName("address_zip")
        @Expose
        public Object addressZip;
        @SerializedName("address_zip_check")
        @Expose
        public Object addressZipCheck;
        @SerializedName("brand")
        @Expose
        public String brand;
        @SerializedName("country")
        @Expose
        public String country;
        @SerializedName("cvc_check")
        @Expose
        public String cvcCheck;
        @SerializedName("dynamic_last4")
        @Expose
        public Object dynamicLast4;
        @SerializedName("exp_month")
        @Expose
        public Integer expMonth;
        @SerializedName("exp_year")
        @Expose
        public Integer expYear;
        @SerializedName("fingerprint")
        @Expose
        public String fingerprint;
        @SerializedName("funding")
        @Expose
        public String funding;
        @SerializedName("last4")
        @Expose
        public String last4;
        @SerializedName("metadata")
        @Expose
        public Metadata metadata;
        @SerializedName("name")
        @Expose
        public Object name;
        @SerializedName("tokenization_method")
        @Expose
        public Object tokenizationMethod;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public Object getAddressCity() {
            return addressCity;
        }

        public void setAddressCity(Object addressCity) {
            this.addressCity = addressCity;
        }

        public Object getAddressCountry() {
            return addressCountry;
        }

        public void setAddressCountry(Object addressCountry) {
            this.addressCountry = addressCountry;
        }

        public Object getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(Object addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public Object getAddressLine1Check() {
            return addressLine1Check;
        }

        public void setAddressLine1Check(Object addressLine1Check) {
            this.addressLine1Check = addressLine1Check;
        }

        public Object getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(Object addressLine2) {
            this.addressLine2 = addressLine2;
        }

        public Object getAddressState() {
            return addressState;
        }

        public void setAddressState(Object addressState) {
            this.addressState = addressState;
        }

        public Object getAddressZip() {
            return addressZip;
        }

        public void setAddressZip(Object addressZip) {
            this.addressZip = addressZip;
        }

        public Object getAddressZipCheck() {
            return addressZipCheck;
        }

        public void setAddressZipCheck(Object addressZipCheck) {
            this.addressZipCheck = addressZipCheck;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCvcCheck() {
            return cvcCheck;
        }

        public void setCvcCheck(String cvcCheck) {
            this.cvcCheck = cvcCheck;
        }

        public Object getDynamicLast4() {
            return dynamicLast4;
        }

        public void setDynamicLast4(Object dynamicLast4) {
            this.dynamicLast4 = dynamicLast4;
        }

        public Integer getExpMonth() {
            return expMonth;
        }

        public void setExpMonth(Integer expMonth) {
            this.expMonth = expMonth;
        }

        public Integer getExpYear() {
            return expYear;
        }

        public void setExpYear(Integer expYear) {
            this.expYear = expYear;
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public void setFingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
        }

        public String getFunding() {
            return funding;
        }

        public void setFunding(String funding) {
            this.funding = funding;
        }

        public String getLast4() {
            return last4;
        }

        public void setLast4(String last4) {
            this.last4 = last4;
        }

        public Metadata getMetadata() {
            return metadata;
        }

        public void setMetadata(Metadata metadata) {
            this.metadata = metadata;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getTokenizationMethod() {
            return tokenizationMethod;
        }

        public void setTokenizationMethod(Object tokenizationMethod) {
            this.tokenizationMethod = tokenizationMethod;
        }

    }

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("object")
        @Expose
        public String object;
        @SerializedName("card")
        @Expose
        public Card card;
        @SerializedName("client_ip")
        @Expose
        public String clientIp;
        @SerializedName("created")
        @Expose
        public Integer created;
        @SerializedName("livemode")
        @Expose
        public Boolean livemode;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("used")
        @Expose
        public Boolean used;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public Card getCard() {
            return card;
        }

        public void setCard(Card card) {
            this.card = card;
        }

        public String getClientIp() {
            return clientIp;
        }

        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }

        public Integer getCreated() {
            return created;
        }

        public void setCreated(Integer created) {
            this.created = created;
        }

        public Boolean getLivemode() {
            return livemode;
        }

        public void setLivemode(Boolean livemode) {
            this.livemode = livemode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Boolean getUsed() {
            return used;
        }

        public void setUsed(Boolean used) {
            this.used = used;
        }

    }
    
}

