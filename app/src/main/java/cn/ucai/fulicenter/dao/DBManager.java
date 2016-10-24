package cn.ucai.fulicenter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.fulicenter.bean.User;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DBManager {
    private static DBManager dbMgr = new DBManager();
    private static DBOpenHelper dbHepler;

    public static synchronized DBManager getInstance() {
        return dbMgr;
    }
    public static DBManager onInit(Context context) {
        if (dbHepler == null) {
            dbHepler = DBOpenHelper.getInstance(context);
        }
        return dbMgr;
    }
    public synchronized void closeDB() {
        if (dbHepler != null) {
            dbHepler.closeDB();
        }
    }
    public boolean saveUser(User user) {
        SQLiteDatabase db = dbHepler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME,user.getMuserName());
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID,user.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUDATE_TIME,user.getMavatarLastUpdateTime());
        if (db.isOpen()) {
            return db.replace(UserDao.USER_TABLE_NAME, null, values) != -1;
        }
        return false;
    }
    public User getUser(String username) {
        return null;
    }
    public boolean updateUser(User user) {
        return false;
    }
}
