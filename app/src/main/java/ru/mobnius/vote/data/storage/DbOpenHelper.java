package ru.mobnius.vote.data.storage;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.storage.models.DaoMaster;

public class DbOpenHelper extends DaoMaster.OpenHelper {

    public DbOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        if(oldVersion == 1 && newVersion == 2) {
            try {
                db.beginTransaction();
                db.execSQL("CREATE TABLE \"cs_street\" (\"ID\" TEXT PRIMARY KEY NOT NULL ,\"C_TYPE\" TEXT,\"C_NAME\" TEXT,\"JB_DATA\" TEXT)");
                db.execSQL("CREATE TABLE \"cd_contacts\" (\"ID\" TEXT PRIMARY KEY NOT NULL ,\"C_FIRST_NAME\" TEXT,\"C_LAST_NAME\" TEXT,\"C_PATRONYMIC\" TEXT,\"FN_STREET\" TEXT,\"C_HOUSE_NUM\" TEXT,\"C_HOUSE_BUILD\" TEXT,\"C_APPARTAMENT\" TEXT,\"N_RATING\" INTEGER,\"C_DESCRIPTION\" TEXT,\"D_DATE\" TEXT,\"FN_USER\" INTEGER NOT NULL ,\"JB_DATA\" TEXT,\"C_PHONE\" TEXT,\"B_DISABLED\" INTEGER NOT NULL ,\"N_ORDER\" INTEGER NOT NULL ,\"OBJECT_OPERATION_TYPE\" TEXT,\"IS_DELETE\" INTEGER NOT NULL ,\"IS_SYNCHRONIZATION\" INTEGER NOT NULL ,\"TID\" TEXT,\"BLOCK_TID\" TEXT)");
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Logger.error(e);
            } finally {
                db.endTransaction();
            }
        }
    }
}
