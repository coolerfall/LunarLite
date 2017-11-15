package com.anbillon.lunarlite.ui.lunar

import com.anbillon.lunarlite.di.scope.PerActivity
import com.anbillon.lunarlite.ui.RxPresenter
import com.coolerfall.lunarlite.domain.repository.AlmanacRepository
import com.coolerfall.widget.lunar.Lunar
import com.coolerfall.widget.lunar.MonthDay
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

/**
 * A presenter controls communication between [LunarLiteView] and models of the presentation.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@PerActivity
class LunarLitePresenter @Inject constructor(
    private val almanacRepository: AlmanacRepository) : RxPresenter<LunarLiteView>() {

  /**
   * Init almanac when user first go into this app.
   */
  fun initAlmanc() {
    val lunar = Lunar.newInstance()
    lunar.setTimeInMillis(System.currentTimeMillis())
    loadAlmanac(MonthDay(lunar.calendar))
  }

  /**
   * Load almanac from database and bind to view.
   *
   * @param monthDay [MonthDay]
   */
  fun loadAlmanac(monthDay: MonthDay) {
    val lunar: Lunar = monthDay.lunar
    disposables.add(
        observeOnView(
            almanacRepository.loadAlmanac(lunar.wielding, lunar.heavenlyAndEarthly).delay(100,
                MILLISECONDS)).subscribe {
          view!!.bindAlmanac(monthDay, it)
        })
  }
}
