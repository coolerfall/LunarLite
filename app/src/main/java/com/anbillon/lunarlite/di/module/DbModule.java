package com.anbillon.lunarlite.di.module;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import com.anbillon.lunarlite.data.db.AssetsDbOpenHelper;
import com.anbillon.lunarlite.data.db.datasource.AlmanacDataSource;
import com.anbillon.lunarlite.domain.repository.AlmanacRepository;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Dagger module that provides database related collaborators.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Module public final class DbModule {
	private static final String DB_NAME = "almanac.db";
	private static final int DB_VERSION = 1;

	@Provides @Singleton SQLiteOpenHelper provideDbOpenHelper(Context context) {
		return new AssetsDbOpenHelper(context, DB_NAME, DB_VERSION);
	}

	@Provides @Singleton SqlBrite provideSqlBrite() {
		return SqlBrite.create(message -> Timber.d(message));
	}

	@Provides @Singleton BriteDatabase provdeBriteDatabase(SqlBrite sqlBrite,
		SQLiteOpenHelper helper) {
		return sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
	}

	@Provides @Singleton AlmanacRepository provideResitory(AlmanacDataSource almanacDataSource) {
		return almanacDataSource;
	}
}
