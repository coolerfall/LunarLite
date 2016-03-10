package com.coolerfall.lunarlite.ui.lunar;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.coolerfall.lunarlite.R;
import com.coolerfall.lunarlite.data.db.model.Almanac;
import com.coolerfall.lunarlite.di.HasComponent;
import com.coolerfall.lunarlite.di.component.DaggerLunarComponent;
import com.coolerfall.lunarlite.di.component.LunarComponent;
import com.coolerfall.lunarlite.di.module.ActivityModule;
import com.coolerfall.lunarlite.ui.BaseActivity;
import com.coolerfall.lunarlite.ui.Presenter;
import com.coolerfall.lunarlite.ui.widget.NoPaddingTextView;
import com.coolerfall.lunarlite.utils.Misc;
import com.coolerfall.widget.lunar.Lunar;
import com.coolerfall.widget.lunar.LunarView;
import com.coolerfall.widget.lunar.MonthDay;
import com.squareup.phrase.Phrase;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * The entrance of this app, show lunar calendar and almanac here.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @since Mar. 05, 2016
 */
public class LunarLiteActivity extends BaseActivity
	implements HasComponent<LunarComponent>, LunarLiteView, LunarView.OnDatePickListener {
	@Bind(R.id.lite_toolbar) Toolbar mToolbar;
	@Bind(R.id.lite_toolbar_tv_title) TextView mTvTitle;
	@Bind(R.id.lite_toolbar_tv_sub_title) TextView mTvSubTitle;
	@Bind(R.id.lite_lunar_view) LunarView mLunarView;
	@Bind(R.id.lite_week_day) TextView mTvWeekDay;
	@Bind(R.id.lite_week_of_year) TextView mTvWeekOfYear;
	@Bind(R.id.lite_cyclical_day) TextView mTvCyclicalDay;
	@Bind(R.id.lite_cyclical_month) TextView mTvCyclicalMonth;
	@Bind(R.id.lite_solar_day) NoPaddingTextView mTvSolarDay;
	@Bind(R.id.lite_lunar_date) TextView mTvLunarDate;
	@Bind(R.id.lite_tv_suit) TextView mTvSuit;
	@Bind(R.id.lite_tv_dread) TextView mTvDread;
	@Bind(R.id.lite_fetus_god) TextView mTvFetusGod;
	@Bind(R.id.lite_star_desc) TextView mTvStarDesc;
	@Bind(R.id.lite_peng_zu_heavenly) TextView mTvPengZuHeavenly;
	@Bind(R.id.lite_peng_zu_earthly) TextView mTvPengZuEarthly;
	@Bind(R.id.lite_evil_spirit) TextView mTvEvilSpirit;
	@Bind(R.id.lite_five_elements) TextView mTvFiveElements;

	@Inject LunarLitePresenter mPresenter;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_lunar_lite;
	}

	@Override
	protected void initView() {
		/* initialize denpendency injection */
		getComponent().inject(this);
		mPresenter.attach(this);

		/* init view */
		mPresenter.initAlmanc();
		mLunarView.setOnDatePickListener(this);

		setSupportActionBar(mToolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayShowTitleEnabled(false);
		}
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_lite, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected Presenter getPresenter() {
		return mPresenter;
	}

	@Override
	public void bindAlmanac(Almanac almanac) {
		mTvSuit.setText(almanac.getSuit());
		mTvDread.setText(almanac.getDread());
	}

	@Override
	public LunarComponent getComponent() {
		return DaggerLunarComponent.builder()
			.appComponent(getAppComponent())
			.activityModule(new ActivityModule(this)).build();
	}

	@Override
	public void onDatePick(LunarView view, MonthDay monthDay) {
		Lunar lunar = monthDay.getLunar();
		Calendar calendar = monthDay.getCalendar();
		mPresenter.loadAlmanac(lunar.getWielding(), lunar.getHeavenlyAndEarthly());

		/* update lunar data */
		CharSequence weekDay = Phrase.from(this, R.string.week_day)
			.put("week_day", lunar.getDayOfWeekInChinese())
			.format();
		CharSequence weekOfYear = Phrase.from(this, R.string.week_of_year)
			.put("week_of_year", calendar.get(Calendar.WEEK_OF_YEAR)).format();
		CharSequence cyclicalDay = Phrase.from(this, R.string.day)
			.put("day", lunar.getCyclicalDay())
			.format();
		CharSequence cyclicalMonth = Phrase.from(this, R.string.month)
			.put("month", lunar.getCyclicalMonth())
			.format();
		CharSequence lunarDate = Phrase.from(this, R.string.lunar_date)
			.put("year", lunar.getCyclicalYear())
			.put("zodiac", lunar.getZodiac())
			.put("month", lunar.getLunarMonth())
			.put("day", lunar.getLunarDay()).format();

		mTvTitle.setText(Misc.formatMillis(calendar.getTimeInMillis(), "yyyy年MM月dd日"));
		mTvSubTitle.setText(lunarDate);
		mTvSolarDay.setText(formatDay(lunar.getSolarDay()));
		mTvWeekDay.setText(weekDay);
		mTvWeekOfYear.setText(weekOfYear);
		mTvCyclicalDay.setText(cyclicalDay);
		mTvCyclicalMonth.setText(cyclicalMonth);
		mTvLunarDate.setText(lunarDate);
		mTvFetusGod.setText(lunar.getFetusGod());
		mTvStarDesc.setText(lunar.getTwentyEightStar());
		mTvPengZuHeavenly.setText(lunar.getPengzu()[0]);
		mTvPengZuEarthly.setText(lunar.getPengzu()[1]);
		mTvEvilSpirit.setText(lunar.getConflictEvilSpirit());
		mTvFiveElements.setText(lunar.getFiveElements());
	}

	@OnClick(R.id.lite_toolbar_layout_title) void onLayoutTitle() {

	}

	/* format day to `0x` if day is less than 10 */
	private String formatDay(int day) {
		String dayString = Integer.toString(day);
		return day < 10 ? "0" + dayString : dayString;
	}
}
