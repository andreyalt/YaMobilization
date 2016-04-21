package com.yamobil.ag.yamobilization.Models;

import java.io.Serializable;

public class Cover implements Serializable {

    private String small;
    private String big;

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getBig() {
        return big;
    }

    public void setBig(String big) {
        this.big = big;
    }
}
