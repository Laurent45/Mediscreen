package com.openclassrooms.mediscreen.report.enumeration;

public enum Level {
    NONE("None"),
    BORDERLINE("Borderline"),
    IN_DANGER("In danger"),
    EARLY_ONSET("Early onset");

    private final String level;

    Level(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}
