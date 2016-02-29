package com.coolerfall.lunarlite.app;

import android.app.Application;

import com.coolerfall.lunarlite.di.HasComponent;
import com.coolerfall.lunarlite.di.component.AppComponent;
import com.coolerfall.lunarlite.di.component.DaggerAppComponent;
import com.coolerfall.lunarlite.di.module.AppModule;

/**
 * This is the lunar application through the whole project.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @since Feb. 20, 2016
 */
public class LunarApplication extends Application implements HasComponent<AppComponent> {
	private AppComponent mAppComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		initInjection();
	}

	@Override
	public AppComponent getComponent() {
		return mAppComponent;
	}

	private void initInjection() {
		mAppComponent = DaggerAppComponent.builder()
			.appModule(new AppModule(this))
			.build();
	}
}
