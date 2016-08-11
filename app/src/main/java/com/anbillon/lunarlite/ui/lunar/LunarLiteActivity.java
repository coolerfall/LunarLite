package com.anbillon.lunarlite.ui.lunar;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.anbillon.lunarlite.data.db.model.Almanac;
import com.anbillon.lunarlite.di.component.DaggerLunarComponent;
import com.anbillon.lunarlite.ui.BaseActivity;
import com.anbillon.lunarlite.ui.widget.NoPaddingTextView;
import com.anbillon.lunarlite.utils.Misc;
import com.coolerfall.lunarlite.R;
import com.coolerfall.widget.lunar.Lunar;
import com.coolerfall.widget.lunar.LunarView;
import com.coolerfall.widget.lunar.MonthDay;
import com.squareup.phrase.Phrase;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * The entrance of this app, show lunar calendar and almanac here.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class LunarLiteActivity extends BaseActivity
	implements LunarLiteView, LunarView.OnDatePickListener {
	@BindView(R.id.lite_toolbar) Toolbar toolbar;
	@BindView(R.id.lite_toolbar_tv_title) TextView tvTitle;
	@BindView(R.id.lite_toolbar_tv_sub_title) TextView tvSubTitle;
	@BindView(R.id.lite_lunar_view) LunarView lunarView;
	@BindView(R.id.lite_week_day) TextView tvWeekDay;
	@BindView(R.id.lite_week_of_year) TextView tvWeekOfYear;
	@BindView(R.id.lite_cyclical_day) TextView tvCyclicalDay;
	@BindView(R.id.lite_cyclical_month) TextView tvCyclicalMonth;
	@BindView(R.id.lite_solar_day) NoPaddingTextView tvSolarDay;
	@BindView(R.id.lite_lunar_date) TextView tvLunarDate;
	@BindView(R.id.lite_tv_suit) TextView tvSuit;
	@BindView(R.id.lite_tv_dread) TextView tvDread;
	@BindView(R.id.lite_fetus_god) TextView tvFetusGod;
	@BindView(R.id.lite_star_desc) TextView tvStarDesc;
	@BindView(R.id.lite_peng_zu_heavenly) TextView tvPengZuHeavenly;
	@BindView(R.id.lite_peng_zu_earthly) TextView tvPengZuEarthly;
	@BindView(R.id.lite_evil_spirit) TextView tvEvilSpirit;
	@BindView(R.id.lite_five_elements) TextView tvFiveElements;

	@Inject LunarLitePresenter presenter;

	@Override protected int layoutResId() {
		return R.layout.activity_lunar_lite;
	}

	@Override protected void initView() {
		/* initialize denpendency injection */
		DaggerLunarComponent.builder().appComponent(getAppComponent()).build().inject(this);
		delegatePresenter(presenter, this);

		/* init view */
		presenter.initAlmanc();
		lunarView.setOnDatePickListener(this);

		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayShowTitleEnabled(false);
		}
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_lite, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override public void bindAlmanac(Almanac almanac) {
		tvSuit.setText(almanac.suit());
		tvDread.setText(almanac.dread());
	}

	@Override public void onDatePick(LunarView view, MonthDay monthDay) {
		Lunar lunar = monthDay.getLunar();
		Calendar calendar = monthDay.getCalendar();
		presenter.loadAlmanac(lunar.getWielding(), lunar.getHeavenlyAndEarthly());

		/* update lunar data */
		CharSequence weekDay = Phrase.from(this, R.string.week_day)
			.put("week_day", lunar.getDayOfWeekInChinese())
			.format();
		CharSequence weekOfYear = Phrase.from(this, R.string.week_of_year)
			.put("week_of_year", calendar.get(Calendar.WEEK_OF_YEAR))
			.format();
		CharSequence cyclicalDay =
			Phrase.from(this, R.string.day).put("day", lunar.getCyclicalDay()).format();
		CharSequence cyclicalMonth =
			Phrase.from(this, R.string.month).put("month", lunar.getCyclicalMonth()).format();
		CharSequence lunarDate = Phrase.from(this, R.string.lunar_date)
			.put("year", lunar.getCyclicalYear())
			.put("zodiac", lunar.getZodiac())
			.put("month", lunar.getLunarMonth())
			.put("day", lunar.getLunarDay())
			.format();

		tvTitle.setText(Misc.formatMillis(calendar.getTimeInMillis(), "yyyy年MM月dd日"));
		tvSubTitle.setText(lunarDate);
		tvSolarDay.setText(formatDay(lunar.getSolarDay()));
		tvWeekDay.setText(weekDay);
		tvWeekOfYear.setText(weekOfYear);
		tvCyclicalDay.setText(cyclicalDay);
		tvCyclicalMonth.setText(cyclicalMonth);
		tvLunarDate.setText(lunarDate);
		tvFetusGod.setText(lunar.getFetusGod());
		tvStarDesc.setText(lunar.getTwentyEightStar());
		tvPengZuHeavenly.setText(lunar.getPengzu()[0]);
		tvPengZuEarthly.setText(lunar.getPengzu()[1]);
		tvEvilSpirit.setText(lunar.getConflictEvilSpirit());
		tvFiveElements.setText(lunar.getFiveElements());
	}

	@OnClick(R.id.lite_toolbar_layout_title) void onLayoutTitle() {

	}

	/* format day to `0x` if day is less than 10 */
	private String formatDay(int day) {
		String dayString = Integer.toString(day);
		return day < 10 ? "0" + dayString : dayString;
	}
}
