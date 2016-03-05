package com.coolerfall.lunarlite.app;

import android.app.Application;

import com.coolerfall.lunarlite.R;
import com.coolerfall.lunarlite.data.db.DaoHelper;
import com.coolerfall.lunarlite.di.HasComponent;
import com.coolerfall.lunarlite.di.component.AppComponent;
import com.coolerfall.lunarlite.di.component.DaggerAppComponent;
import com.coolerfall.lunarlite.di.module.AppModule;
import com.coolerfall.lunarlite.utils.Misc;

import java.io.File;

import timber.log.Timber;

/**
 * This is the lunar application through the whole project.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @since Feb. 20, 2016
 */
public class LunarApplication extends Application implements HasComponent<AppComponent> {
	private static final String TAG = "Vtag";
	private AppComponent mAppComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		/* copy database if not exist */
		copyDatabase();
		/* init injection */
		initInjection();

		/* config timber logger */
		Timber.tag(TAG);
		Timber.plant(new Timber.DebugTree());
	}

	@Override
	public AppComponent getComponent() {
		return mAppComponent;
	}

	/* init dependency injection */
	private void initInjection() {
		mAppComponent = DaggerAppComponent.builder()
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
