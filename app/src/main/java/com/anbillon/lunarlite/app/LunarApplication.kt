package com.anbillon.lunarlite.app

import android.app.Activity
import android.app.Application
import com.anbillon.lunarlite.BuildConfig
import com.anbillon.lunarlite.di.component.DaggerAppComponent
import com.anbillon.lunarlite.di.module.AppModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * This is the lunar application through the whole project.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
class LunarApplication : Application(), HasActivityInjector {
  @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

  override fun onCreate() {
    super.onCreate()
    DaggerAppComponent.builder().appModule(AppModule(this)).build().inject(this)

    /* config timber logger */
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

  override fun activityInjector(): AndroidInjector<Activity> {
    return activityInjector
  }
}
