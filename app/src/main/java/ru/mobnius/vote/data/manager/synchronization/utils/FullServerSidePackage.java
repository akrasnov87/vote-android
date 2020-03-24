package ru.mobnius.vote.data.manager.synchronization.utils;

/**
 * Обработчик пакетов в оба направления с удалением существующих записей
 */
public class FullServerSidePackage extends ServerSidePackage {
    public FullServerSidePackage(){
        setDeleteRecordBeforeAppend(true);
    }
}
