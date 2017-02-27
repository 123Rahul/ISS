package com.ambeyindustry.iss;

public class People {
    String name, craft;

    public People(String craft, String name) {
        this.craft = craft;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCraft() {
        return craft;
    }
}
