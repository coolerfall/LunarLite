package com.anbillon.lunarlite.di.component


import com.anbillon.lunarlite.app.LunarApplication
import com.anbillon.lunarlite.di.module.AppModule
import com.anbillon.lunarlite.di.module.android.ActivityModule
import com.coolerfall.lunarlite.domain.repository.AlmanacRepository
import dagger.Component
import javax.inject.Singleton

/**
 * A application-level component used in this projct.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, ActivityModule::class))
interface AppComponent {
  /**
   * Provides [AlmanacRepository] for other component to juse.
   *
   * @return [AlmanacRepository]
   */
  fun almanacRepository(): AlmanacRepository

  /**
   * Inject into [LunarApplication].
   *
   * @param application [LunarApplication]
   */
  fun inject(application: LunarApplication)
}
