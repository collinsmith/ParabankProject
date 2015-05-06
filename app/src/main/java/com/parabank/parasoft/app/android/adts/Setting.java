package com.parabank.parasoft.app.android.adts;

public class Setting {
    private final String id;
    private final int name;
    private final int hint;
    private final int inputType;
    private final String defaultValue;

    public Setting(String id, int name, int hint, int inputType, String defaultValue) {
        this.id = id;
        this.name = name;
        this.hint = hint;
        this.inputType = inputType;
        this.defaultValue = defaultValue;
    }

    public String getId() {
        return id;
    }

    public int getName() {
        return name;
    }

    public int getHint() {
        return hint;
    }

    public int getInputType() {
        return inputType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
