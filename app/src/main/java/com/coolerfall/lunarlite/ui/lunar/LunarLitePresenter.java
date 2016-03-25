package com.coolerfall.lunarlite.ui.lunar;

import com.coolerfall.lunarlite.di.scope.PerActivity;
import com.coolerfall.lunarlite.domain.interactor.AlmanacUsecase;
import com.coolerfall.lunarlite.ui.Presenter;
import com.coolerfall.lunarlite.ui.View;
import com.coolerfall.widget.lunar.Lunar;

import javax.inject.Inject;

import rx.Subscription;

/**
 * A presenter controls communication between {@link LunarLiteView} and models of the presentation.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@PerActivity
public class LunarLitePresenter implements Presenter {
	private AlmanacUsecase mAlmanacUsecase;
	private Subscription mAlmanacSubscription;
	private LunarLiteView mView;

	@Inject
	public LunarLitePresenter(AlmanacUsecase almanacUsecase) {
		mAlmanacUsecase = almanacUsecase;
	}

	@Override
	public void attach(View view) {
		mView = (LunarLiteView) view;
	}

	@Override
	public void resume() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void destroy() {
		if (mAlmanacSubscription != null && mAlmanacSubscription.isUnsubscribed()) {
			mAlmanacSubscription.unsubscribe();
		}
	}

	/**
	 * Init almanac when user first go into this app.
	 */
	protected void initAlmanc() {
		Lunar lunar = Lunar.newInstance();
		lunar.setTimeInMillis(System.currentTimeMillis());
		loadAlmanac(lunar.getWielding(), lunar.getHeavenlyAndEarthly());
	}

	/**
	 * Load almanac from database and bind to view.
	 *
	 * @param wielding        wielding of god
	 * @param heavenlyEarthly heavenly stems and earthly branches
	 */
	protected void loadAlmanac(int wielding, int heavenlyEarthly) {
		mAlmanacSubscription = mAlmanacUsecase.getAuspiciousDay(wielding, heavenlyEarthly)
			.filter(almanac -> almanac != null).subscribe(mView::bindAlmanac);
	}
}
