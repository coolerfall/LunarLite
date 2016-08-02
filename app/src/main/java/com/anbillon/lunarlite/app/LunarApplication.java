package com.anbillon.lunarlite.app;

import android.app.Application;

import com.anbillon.lunarlite.data.db.DaoHelper;
import com.anbillon.lunarlite.di.component.DaggerAppComponent;
import com.anbillon.lunarlite.di.module.AppModule;
import com.coolerfall.lunarlite.BuildConfig;
import com.coolerfall.lunarlite.R;
import com.anbillon.lunarlite.di.HasComponent;
import com.anbillon.lunarlite.di.component.AppComponent;
import com.anbillon.lunarlite.utils.Misc;

import java.io.File;

import timber.log.Timber;

/**
 * This is the lunar application through the whole project.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class LunarApplication extends Application implements HasComponent<AppComponent> {
	private static final String TAG = "LunarLite";
	private AppComponent appComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		/* copy database if not exist */
		copyDatabase();
		/* init injection */
		initInjection();

		/* config timber logger */
		Timber.tag(TAG);
		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		}
	}

	@Override
	public AppComponent getComponent() {
		return appComponent;
	}

	/* init dependency injection */
	private void initInjection() {
		appComponent = DaggerAppComponent.builder()
			.appModule(new AppModule(this))
			.build();
	}

	/* copy almanac database if not exist */
	private void copyDatabase() {
		File dbFile = getDatabasePath(DaoHelper.DB_NAME);
		if (!dbFile.exists()) {
			Misc.copyRawDatabase(this, R.raw.almanac, DaoHelper.DB_NAME);
		}
	}
}
