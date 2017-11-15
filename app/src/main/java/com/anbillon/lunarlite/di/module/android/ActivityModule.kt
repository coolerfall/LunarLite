package com.anbillon.lunarlite.di.module.android

import android.app.Activity
import com.anbillon.lunarlite.di.component.AndroidComponent.LunarComponent
import com.anbillon.lunarlite.ui.lunar.LunarLiteActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Module(subcomponents = arrayOf(LunarComponent::class)) abstract class ActivityModule {
  @Binds
  @IntoMap
  @ActivityKey(LunarLiteActivity::class) internal abstract fun bindAlmanac(
      builder: LunarComponent.Builder): AndroidInjector.Factory<out Activity>

}