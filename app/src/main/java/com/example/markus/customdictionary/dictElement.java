package com.example.markus.customdictionary;

import android.support.annotation.NonNull;

public class dictElement implements Comparable<dictElement> {
    private int familiarity;
    private int times_displayed;
    private boolean selected;
    private String entry;
    private SortingType type;

    public dictElement(String entry, int familiarity, int times_displayed, SortingType type) {
        this.familiarity = familiarity;
        this.times_displayed = times_displayed;
        this.entry = entry;
        this.type = type;
    }

    public SortingType getSortingType() {
        return this.type;
    }

    public void setSortingType(SortingType type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setFamiliarity(int familiarity) {
        this.familiarity = familiarity;
    }

    public int getTimes_displayed() {
        return times_displayed;
    }

    public void setTimes_displayed(int times_displayed) {
        this.times_displayed = times_displayed;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public SortingType getType() {
        return type;
    }

    public void setType(SortingType type) {
        this.type = type;
    }

    public String getEntry() {
        return this.entry;
    }

    public int getFamiliarity() {
        return this.familiarity;
    }

    @Override
    public int compareTo(@NonNull dictElement o) {

        if (type == SortingType.FAMILIARITY) {
            if (this.familiarity > o.familiarity) {
                return 1;
            } else if (this.familiarity == o.familiarity) {
                return 0;
            } else {
                return -1;
            }
        } else if (type == SortingType.BY_TIMES_DISPLAYED) {
            if (this.times_displayed > o.times_displayed) {
                return 1;
            } else if (this.times_displayed == o.times_displayed) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return this.entry.toLowerCase().compareTo(o.entry.toLowerCase());
        }

    }
}
