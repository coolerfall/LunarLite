package com.anbillon.lunarlite.app;

import android.app.Application;
import com.anbillon.lunarlite.di.HasComponent;
import com.anbillon.lunarlite.di.component.AppComponent;
import com.anbillon.lunarlite.di.component.DaggerAppComponent;
import com.anbillon.lunarlite.di.module.AppModule;
import com.coolerfall.lunarlite.BuildConfig;
import timber.log.Timber;

/**
 * This is the lunar application through the whole project.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class LunarApplication extends Application implements HasComponent<AppComponent> {
	private static final String TAG = "LunarLite";
	private AppComponent appComponent;

	@Override public void onCreate() {
		super.onCreate();

		/* init injection */
		initInjection();

		/* config timber logger */
		Timber.tag(TAG);
		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		}
	}

	@Override public AppComponent getComponent() {
		return appComponent;
	}

	/* init dependency injection */
	private void initInjection() {
		appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
	}
}
