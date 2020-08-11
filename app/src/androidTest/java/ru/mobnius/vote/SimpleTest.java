package ru.mobnius.vote;

import ru.mobnius.vote.data.GlobalSettings;

public abstract class SimpleTest {
    public SimpleTest() {
        GlobalSettings.ENVIRONMENT = "test";
    }
}
