package ru.mobnius.vote.data.storage.models;

interface IEntityTo {
    String getObjectOperationType();
    boolean getIsDelete();
    boolean getIsSynchronization();
    String getTid();
    String getBlockTid();
}
