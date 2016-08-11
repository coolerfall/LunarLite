package com.anbillon.lunarlite.ui;

import android.os.Bundle;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * This class represents a delegate for {@link Presenter} so {@link BaseActivity} can control the
 * given {@link Presenter}.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class PresenterDelegate<T extends BaseView> implements Presenter<T> {
	private Presenter<T> presenter;

	public PresenterDelegate(Presenter<T> presenter) {
		this.presenter = checkNotNull(presenter, "presenter == null");
	}

	public void delegate(Presenter<T> presenter) {
		this.presenter = checkNotNull(presenter, "presenter == null");
	}

	@Override public void attach(T view) {
		presenter.attach(view);
	}

	@Override public void restoreInstanceState(Bundle savedInstanceState) {
		presenter.restoreInstanceState(savedInstanceState);
	}

	@Override public void saveInstanceState(Bundle outState) {
		presenter.saveInstanceState(outState);
	}

	@Override public void detach() {
		presenter.detach();
	}
}
