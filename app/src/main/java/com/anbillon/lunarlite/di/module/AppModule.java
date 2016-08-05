package com.anbillon.lunarlite.di.module;

import android.content.Context;
import com.anbillon.lunarlite.app.LunarApplication;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Dagger module that provides app level related collaborators.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Module(includes = DbModule.class) public final class AppModule {
	private final LunarApplication lunarApplication;

	public AppModule(LunarApplication lunarApplication) {
		this.lunarApplication = lunarApplication;
	}

	@Provides @Singleton Context provideAppContext() {
		return lunarApplication.getApplicationContext();
	}
}
