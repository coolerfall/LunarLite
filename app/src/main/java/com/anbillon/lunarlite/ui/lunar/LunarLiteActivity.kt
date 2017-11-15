package com.anbillon.lunarlite.ui.lunar

import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.anbillon.lunarlite.R
import com.anbillon.lunarlite.data.db.model.Almanac
import com.anbillon.lunarlite.ui.BaseActivity
import com.anbillon.lunarlite.ui.widget.NoPaddingTextView
import com.anbillon.lunarlite.utils.Misc
import com.coolerfall.widget.lunar.Lunar
import com.coolerfall.widget.lunar.LunarView
import com.coolerfall.widget.lunar.MonthDay
import com.squareup.phrase.Phrase
import java.util.Calendar
import javax.inject.Inject

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
class LunarLiteActivity : BaseActivity(), LunarLiteView, LunarView.OnDatePickListener {
  @BindView(R.id.lite_toolbar) lateinit var toolbar: Toolbar
  @BindView(R.id.lite_toolbar_tv_title) lateinit var tvTitle: TextView
  @BindView(R.id.lite_toolbar_tv_sub_title) lateinit var tvSubTitle: TextView
  @BindView(R.id.lite_lunar_view) lateinit var lunarView: LunarView
  @BindView(R.id.lite_week_day) lateinit var tvWeekDay: TextView
  @BindView(R.id.lite_week_of_year) lateinit var tvWeekOfYear: TextView
  @BindView(R.id.lite_cyclical_day) lateinit var tvCyclicalDay: TextView
  @BindView(R.id.lite_cyclical_month) lateinit var tvCyclicalMonth: TextView
  @BindView(R.id.lite_solar_day) lateinit var tvSolarDay: NoPaddingTextView
  @BindView(R.id.lite_lunar_date) lateinit var tvLunarDate: TextView
  @BindView(R.id.lite_tv_suit) lateinit var tvSuit: TextView
  @BindView(R.id.lite_tv_dread) lateinit var tvDread: TextView
  @BindView(R.id.lite_fetus_god) lateinit var tvFetusGod: TextView
  @BindView(R.id.lite_star_desc) lateinit var tvStarDesc: TextView
  @BindView(R.id.lite_peng_zu_heavenly) lateinit var tvPengZuHeavenly: TextView
  @BindView(R.id.lite_peng_zu_earthly) lateinit var tvPengZuEarthly: TextView
  @BindView(R.id.lite_evil_spirit) lateinit var tvEvilSpirit: TextView
  @BindView(R.id.lite_five_elements) lateinit var tvFiveElements: TextView

  @Inject lateinit var presenter: LunarLitePresenter

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_lite, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun initView() {
    setContentView(R.layout.activity_lunar_lite)
    ButterKnife.bind(this)

    presenter.attach(this)
    presenter.initAlmanc()
    lunarView.setOnDatePickListener(this)

    setSupportActionBar(toolbar)
    if (supportActionBar != null) {
      supportActionBar!!.setDisplayShowTitleEnabled(false)
    }
  }

  override fun bindAlmanac(monthDay: MonthDay, almanac: Almanac) {
    val lunar: Lunar = monthDay.lunar
    val calendar: Calendar = monthDay.calendar

    /* update lunar data */
    val weekDay: CharSequence = Phrase.from(this, R.string.week_day).put("week_day",
        lunar.dayOfWeekInChinese).format()
    val weekOfYear: CharSequence = Phrase.from(this, R.string.week_of_year).put("week_of_year",
        calendar.get(Calendar.WEEK_OF_YEAR)).format()
    val cyclicalDay: CharSequence = Phrase.from(this, R.string.day).put("day",
        lunar.cyclicalDay).format()
    val cyclicalMonth: CharSequence = Phrase.from(this, R.string.month).put("month",
        lunar.cyclicalMonth).format()
    val lunarDate: CharSequence = Phrase.from(this, R.string.lunar_date).put("year",
        lunar.cyclicalYear).put("zodiac", lunar.zodiac).put("month", lunar.lunarMonth).put("day",
        lunar.lunarDay).format()

    tvTitle.text = Misc.formatMillis(calendar.timeInMillis, "yyyy年MM月dd日")
    tvSubTitle.text = lunarDate
    tvSolarDay.setText(formatDay(lunar.solarDay))
    tvWeekDay.text = weekDay
    tvWeekOfYear.text = weekOfYear
    tvCyclicalDay.text = cyclicalDay
    tvCyclicalMonth.text = cyclicalMonth
    tvLunarDate.text = lunarDate
    tvFetusGod.text = lunar.fetusGod
    tvStarDesc.text = lunar.twentyEightStar
    tvPengZuHeavenly.text = lunar.pengzu[0]
    tvPengZuEarthly.text = lunar.pengzu[1]
    tvEvilSpirit.text = lunar.conflictEvilSpirit
    tvFiveElements.text = lunar.fiveElements

    tvSuit.text = almanac.suit()
    tvDread.text = almanac.dread()
  }

  override fun onDatePick(view: LunarView, monthDay: MonthDay) {
    presenter.loadAlmanac(monthDay)
  }

  /* format day to `0x` if day is less than 10 */
  private fun formatDay(day: Int): String {
    val dayString = Integer.toString(day)
    return if (day < 10) ("0" + dayString) else dayString
  }
}

