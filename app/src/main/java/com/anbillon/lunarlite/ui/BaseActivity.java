package com.anbillon.lunarlite.ui;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.anbillon.lunarlite.app.LunarApplication;
import com.anbillon.lunarlite.di.component.AppComponent;

import butterknife.ButterKnife;

/**
 * Base activity of this project, all activity must inherit from this activity.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public abstract class BaseActivity extends AppCompatActivity {
	private static final RxPresenter RX_PRESENTER = new RxPresenter() {
	};
	@SuppressWarnings("unchecked") private PresenterDelegate presenterDelegate =
		new PresenterDelegate(RX_PRESENTER);

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
	protected void onDestroy() {
		super.onDestroy();
		presenterDelegate.detach();
		ButterKnife.unbind(this);
	}

	@Override protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		presenterDelegate.saveInstanceState(outState);
	}

	@Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		presenterDelegate.restoreInstanceState(savedInstanceState);
	}

	@Override public Resources getResources() {
		Resources resources = super.getResources();
		Configuration configuration = new Configuration();
		configuration.setToDefaults();
		resources.updateConfiguration(configuration, resources.getDisplayMetrics());
		return resources;
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
	 * Delegate given {@link Presenter} so {@link BaseActivity} can control it.
	 *
	 * @param presenter {@link Presenter}
	 * @param view {@link BaseView}
	 */
	@SuppressWarnings("unchecked") protected <T extends BaseView> void delegatePresenter(
		Presenter<T> presenter, T view) {
		presenterDelegate.delegate(presenter);
		presenterDelegate.attach(view);
	}

	/**
	 * Get {@link AppComponent} in {@link LunarApplication}.
	 *
	 * @return {@link AppComponent}
	 */
	protected AppComponent getAppComponent() {
		return ((LunarApplication) getApplication()).getComponent();
	}
}
