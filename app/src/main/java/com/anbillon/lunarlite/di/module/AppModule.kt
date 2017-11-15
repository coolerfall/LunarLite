package com.anbillon.lunarlite.di.module

import android.content.Context
import com.anbillon.lunarlite.app.LunarApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Application module used in this project.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Module(includes = arrayOf(DbModule::class))
class AppModule(
    private val application: LunarApplication) {

  @Provides
  @Singleton internal fun provideAppContext(): Context {
    return application.applicationContext
  }
}
