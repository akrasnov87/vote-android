package ru.mobnius.vote.data.manager;

import org.junit.Test;

import static org.junit.Assert.*;

public class DbOperationTypeTest {
    @Test
    public void equalString() {
        assertEquals(DbOperationType.CREATED, "CREATED");
        assertEquals(DbOperationType.UPDATED, "UPDATED");
        assertEquals(DbOperationType.REMOVED, "REMOVED");
    }
}