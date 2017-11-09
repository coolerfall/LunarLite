package com.anbillon.lunarlite.ui.lunar

import com.anbillon.lunarlite.data.db.model.Almanac
import com.anbillon.lunarlite.ui.BaseView
import com.coolerfall.widget.lunar.MonthDay

/**
 * Used as a view representing a lunar view.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
interface LunarLiteView : BaseView {
  /**
   * Bind almanac information to this view.
   *
   * @param monthDay [MonthDay]
   * @param almanac [Almanac]
   */
  fun bindAlmanac(monthDay: MonthDay, almanac: Almanac)
}
