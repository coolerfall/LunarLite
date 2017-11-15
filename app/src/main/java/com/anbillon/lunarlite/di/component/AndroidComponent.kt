package com.anbillon.lunarlite.di.component

import com.anbillon.lunarlite.di.scope.PerActivity
import com.anbillon.lunarlite.ui.lunar.LunarLiteActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
object AndroidComponent {
  @PerActivity
  @Subcomponent
  interface LunarComponent : AndroidInjector<LunarLiteActivity> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<LunarLiteActivity>()
  }
}