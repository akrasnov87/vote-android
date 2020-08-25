package ru.mobnius.vote.data.manager.synchronization.utils;

/**
 * Обработчик пакетов в оба направления с удалением существующих записей
 */
public class LiteServerSidePackage extends FullServerSidePackage {
    public LiteServerSidePackage() {
        super();
        setDeleteRecordBeforeAppend(false);
    }
}
