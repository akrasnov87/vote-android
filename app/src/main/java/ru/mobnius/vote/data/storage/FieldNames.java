package ru.mobnius.vote.data.storage;

import ru.mobnius.vote.data.storage.models.AuditsDao;

/**
 * Имена полей для запросов
 */
// TODO: 27/12/2019 потом убрать и перенести в Names
public interface FieldNames {
    String BLOCK_TID = AuditsDao.Properties.BlockTid.columnName;
    String TID = AuditsDao.Properties.Tid.columnName;
    String IS_SYNCHRONIZATION = AuditsDao.Properties.IsSynchronization.columnName;
    String OBJECT_OPERATION_TYPE = AuditsDao.Properties.ObjectOperationType.columnName;
}
