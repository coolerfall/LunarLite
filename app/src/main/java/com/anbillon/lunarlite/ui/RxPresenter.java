package com.anbillon.lunarlite.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;

import rx.subscriptions.CompositeSubscription;

/**
 * Abstract class representing a Presenter with rxJava in a model view presenter (MVP) pattern.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public abstract class RxPresenter<T extends BaseView> implements Presenter<T> {
	protected final CompositeSubscription subscriptions = new CompositeSubscription();
	protected T view;

	@CallSuper @Override public void attach(T view) {
		this.view = view;
	}

	@Override public void restoreInstanceState(Bundle savedInstanceState) {
	}

	@Override public void saveInstanceState(Bundle outState) {
	}

	@CallSuper @Override public void detach() {
		subscriptions.clear();
		view = null;
	}
}