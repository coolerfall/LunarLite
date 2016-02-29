package com.coolerfall.lunarlite.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Base activity of this project, all activity must inherit from this activity.
 *
 * @author Vincent Cheung
 * @since  Aug. 05, 2015
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
}
