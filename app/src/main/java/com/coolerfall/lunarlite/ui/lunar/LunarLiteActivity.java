package com.coolerfall.lunarlite.ui.lunar;

import android.os.Bundle;

import com.coolerfall.lunarlite.R;
import com.coolerfall.lunarlite.data.db.model.Almanac;
import com.coolerfall.lunarlite.di.HasComponent;
import com.coolerfall.lunarlite.di.component.DaggerLunarComponent;
import com.coolerfall.lunarlite.di.component.LunarComponent;
import com.coolerfall.lunarlite.di.module.ActivityModule;
import com.coolerfall.lunarlite.ui.BaseActivity;
import com.coolerfall.lunarlite.ui.Presenter;
import com.coolerfall.widget.lunar.Lunar;
import com.coolerfall.widget.lunar.LunarView;
import com.coolerfall.widget.lunar.MonthDay;

import javax.inject.Inject;

import butterknife.Bind;
import timber.log.Timber;

public class LunarLiteActivity extends BaseActivity
	implements HasComponent<LunarComponent>, LunarLiteView, LunarView.OnDatePickListener {
	@Bind(R.id.lunar_view) LunarView mLunarView;
	@Inject LunarLitePresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lunar);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_lunar;
	}

	@Override
	protected void initView() {
		/* initialize denpendency injection */
		getComponent().inject(this);
		mPresenter.attach(this);

		/* init view */
		mLunarView.setOnDatePickListener(this);
		mPresenter.initAlmanc();
	}

	@Override
	protected Presenter getPresenter() {
		return mPresenter;
	}

	@Override
	public void bindAlmanac(Almanac almanac) {
		Timber.d("timber bindAlmanac");
	}

	@Override
	public LunarComponent getComponent() {
		return DaggerLunarComponent.builder()
			.appComponent(getAppComponent())
			.activityModule(new ActivityModule(this)).build();
	}

	@Override public void onDatePick(LunarView view, MonthDay monthDay) {
		Lunar lunar = monthDay.getLunar();
		mPresenter.loadAlmanac(lunar.getWielding(), lunar.getHeavenlyAndEarthly());
	}
}
