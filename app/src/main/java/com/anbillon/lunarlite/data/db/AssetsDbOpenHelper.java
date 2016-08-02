package com.anbillon.lunarlite.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import timber.log.Timber;

/**
 * A sqlite database open helper for external database in assets folder.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class AssetsDbOpenHelper extends SQLiteOpenHelper {
	private final Context context;
	private final String dbName;
	private boolean shouldUpgrade;
	private int oldVersion;

	public AssetsDbOpenHelper(Context context, String dbName, int version) {
		super(context, dbName, null, version);
		this.context = context;
		this.dbName = dbName;
	}

	/**
	 * Copy database in raw resource folder into app database folder.
	 *
	 * @param context the context to use
	 * @param dbName database name
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored") static void copyAssetsDb(Context context,
		String dbName) throws IOException {
		File dbFile = context.getDatabasePath(dbName);
		File parentFile = dbFile.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}

		FileOutputStream fos = new FileOutputStream(dbFile);
		InputStream is = context.getAssets().open(dbName);
		byte[] buffer = new byte[2048];
		int count;
		while ((count = is.read(buffer)) > 0) {
			fos.write(buffer, 0, count);
		}
	}

	/**
	 * Delete db file by name.
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored") static void deleteDb(Context context,
		String dbName) {
		File file = context.getDatabasePath(dbName);
		file.delete();
	}

	/**
	 * To check if database existed.
	 *
	 * @param context the context to use
	 * @param dbName database name
	 * @return true if existed, otherwise return false
	 */
	static boolean isDbExisted(Context context, String dbName) {
		File dbFile = context.getDatabasePath(dbName);
		return dbFile.exists();
	}

	@Override public SQLiteDatabase getReadableDatabase() {
		if (!isDbExisted(context, dbName)) {
			try {
				copyAssetsDb(context, dbName);
				Timber.i("First open and copy");
			} catch (IOException e) {
				deleteDb(context, dbName);
				e.printStackTrace();
				Timber.e(e, "First open and fail to copy db, %s", e.getMessage());
			}
			return super.getReadableDatabase();
		} else {
			SQLiteDatabase database = super.getReadableDatabase();
			boolean shouldDowngrade = false;
			if (shouldUpgrade) {
				database.close();
				try {
					copyAssetsDb(context, dbName);
					shouldUpgrade = false;
					Timber.i("External database upgrade, new version: %d", database.getVersion());
				} catch (IOException e) {
					shouldDowngrade = true;
					deleteDb(context, dbName);
					Timber.e(e, "Upgrade and fail to copy db, %s", e.getMessage());
				}
				database = super.getReadableDatabase();
				if (shouldDowngrade) {
					database.setVersion(oldVersion);
				}
			}
			return database;
		}
	}

	@Override public void onCreate(SQLiteDatabase db) {
	}

	@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.oldVersion = oldVersion;
		shouldUpgrade = newVersion > oldVersion;
	}
}
