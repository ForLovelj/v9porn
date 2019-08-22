package com.u9porn.data.db;

import android.content.Context;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

import com.u9porn.data.db.entity.CategoryDao;
import com.u9porn.data.db.entity.DaoMaster;
import com.u9porn.data.db.entity.V9PornItemDao;
import com.u9porn.data.db.entity.VideoResultDao;
import com.u9porn.di.ApplicationContext;
import com.u9porn.di.DatabaseInfo;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author flymegoc
 * @date 2018/1/13
 */
@Singleton
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {

    @Inject
    public MySQLiteOpenHelper(@ApplicationContext Context context, @DatabaseInfo String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, V9PornItemDao.class, VideoResultDao.class, CategoryDao.class);
    }
}

