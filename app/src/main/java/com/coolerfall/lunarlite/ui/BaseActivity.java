package com.coolerfall.lunarlite.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.coolerfall.lunarlite.app.LunarApplication;
import com.coolerfall.lunarlite.di.component.AppComponent;

import butterknife.ButterKnife;

/**
 * Base activity of this project, all activity must inherit from this activity.
 *
 * @author Vincent Cheung
 * @since Aug. 05, 2015
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseFragment.OnBackHandler {
	private BaseFragment mCurrentFtagment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getLayoutResID() != 0) {
			setContentView(getLayoutResID());
		}

		ButterKnife.bind(this);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPresenter().resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPresenter().pause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getPresenter().destroy();
		ButterKnife.unbind(this);
	}

	@Override
	public void onBackPressed() {
		if (mCurrentFtagment == null || !mCurrentFtagment.onBackPressed()) {
			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
				super.onBackPressed();
			} else {
				getSupportFragmentManager().popBackStack();
			}
		}
	}

	@Override
	public void setCurrentFragment(BaseFragment fragment) {
		mCurrentFtagment = fragment;
	}

	/**
	 * Get layout resource id of inherited activity.
	 */
	protected abstract @LayoutRes int getLayoutResID();

	/**
	 * Initialize view in inherited activity.
	 */
	protected abstract void initView();

	/**
	 * Get {@link Presenter} to use.
	 *
	 * @return {@link Presenter}
	 */
	protected abstract Presenter getPresenter();

	/**
	 * Get {@link AppComponent} in {@link LunarApplication}.
	 *
	 * @return {@link AppComponent}
	 */
	protected AppComponent getAppComponent() {
		return ((LunarApplication) getApplication()).getComponent();
	}
}
