package com.coolerfall.lunarlite.app;

import android.app.Application;

import com.coolerfall.lunarlite.di.HasComponent;
import com.coolerfall.lunarlite.di.component.AppComponent;
import com.coolerfall.lunarlite.di.component.DaggerAppComponent;
import com.coolerfall.lunarlite.di.module.AppModule;

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

		initInjection();

		/* config timber logger */
		Timber.plant(new Timber.DebugTree());
		Timber.tag(TAG);
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
}
