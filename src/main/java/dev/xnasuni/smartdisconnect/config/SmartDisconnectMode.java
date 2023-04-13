package dev.xnasuni.smartdisconnect.config;

import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;

public enum SmartDisconnectMode implements SelectionListEntry.Translatable {
    CheckboxType("Checkbox");

    private final String name;

    SmartDisconnectMode(String name) {
        this.name = name;
    }

    public String getKey() {
        return this.name;
    }
}
