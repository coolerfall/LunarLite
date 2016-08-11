package com.anbillon.lunarlite.di.module;

import android.app.Activity;
import android.content.Context;

import com.anbillon.lunarlite.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides activity level related collaborators.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Module public final class ActivityModule {
	private final Activity mActivity;

	public ActivityModule(Activity activity) {
		mActivity = activity;
	}

	@Provides @PerActivity Context provideActivityContext() {
		return mActivity;
	}
}
