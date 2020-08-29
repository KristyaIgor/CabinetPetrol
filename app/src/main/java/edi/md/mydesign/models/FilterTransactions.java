package edi.md.mydesign.models;


/**
 * Created by Igor on 18.08.2020
 */

public class FilterTransactions {
    private boolean all = true, feeding = false, refill = false;
    private boolean day = true, week = false, month = false, year = false;

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public boolean isFeeding() {
        return feeding;
    }

    public void setFeeding(boolean feeding) {
        this.feeding = feeding;
    }

    public boolean isRefill() {
        return refill;
    }

    public void setRefill(boolean refill) {
        this.refill = refill;
    }

    public boolean isDay() {
        return day;
    }

    public void setDay(boolean day) {
        this.day = day;
    }

    public boolean isWeek() {
        return week;
    }

    public void setWeek(boolean week) {
        this.week = week;
    }

    public boolean isMonth() {
        return month;
    }

    public void setMonth(boolean month) {
        this.month = month;
    }

    public boolean isYear() {
        return year;
    }

    public void setYear(boolean year) {
        this.year = year;
    }
}
