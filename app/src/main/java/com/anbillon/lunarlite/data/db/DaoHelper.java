package com.anbillon.lunarlite.data.db;

import android.content.Context;

import com.anbillon.lunarlite.data.db.model.DaoMaster;
import com.anbillon.lunarlite.data.db.model.DaoSession;

/**
 * Dao helper: handle dao here.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class DaoHelper {
	public static final String DB_NAME = "almanac.db";
	private static DaoSession sSession;

	private DaoHelper() {

	}

	/**
	 * Get dao session.
	 *
	 * @param context context
	 * @return dao session
	 */
	public static DaoSession getDaoSession(Context context) {
		synchronized (DaoHelper.class) {
			if (sSession == null) {
				DbOpenHelper openHelper = new DbOpenHelper(context, DB_NAME);
				DaoMaster daoMaster = new DaoMaster(openHelper.getWritableDatabase());

				sSession = daoMaster.newSession();
			}
		}

		return sSession;
	}
}
