package com.anbillon.lunarlite.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.anbillon.lunarlite.data.db.model.DaoMaster;

/**
 * Database open helper used in this app.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class DbOpenHelper extends DaoMaster.OpenHelper {

	public DbOpenHelper(Context context, String dbName) {
		this(context, dbName, null);
	}

	public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
		super(context, name, factory);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 2:
			DaoMaster.dropAllTables(db, true);
			DaoMaster.createAllTables(db, true);
			break;

		default:
			break;
		}
	}
}