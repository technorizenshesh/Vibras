package com.my.vibras.model;

public class PaymentModel {

    String name;
    String nameONe;
    int img;

    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public PaymentModel(String name, String nameONe, int img) {
        this.name = name;
        this.nameONe = nameONe;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameONe() {
        return nameONe;
    }

    public void setNameONe(String nameONe) {
        this.nameONe = nameONe;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }




}
