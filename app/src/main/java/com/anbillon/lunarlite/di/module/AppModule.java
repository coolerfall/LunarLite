package com.anbillon.lunarlite.di.module;

import android.content.Context;

import com.anbillon.lunarlite.app.LunarApplication;
import com.anbillon.lunarlite.data.db.repository.AlmanacDataSource;
import com.anbillon.lunarlite.domain.repository.AlmanacRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Application module used in this project.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Module
public class AppModule {
	private final LunarApplication mLunarApplication;

	public AppModule(LunarApplication lunarApplication) {
		mLunarApplication = lunarApplication;
	}

	@Provides @Singleton Context provideAppContext() {
		return mLunarApplication.getApplicationContext();
	}

	@Provides @Singleton AlmanacRepository provideAlmanacRepository(AlmanacDataSource dataSource) {
		return dataSource;
	}
}
