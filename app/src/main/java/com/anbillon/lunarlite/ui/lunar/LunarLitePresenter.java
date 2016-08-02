package com.anbillon.lunarlite.ui.lunar;

import com.anbillon.lunarlite.di.scope.PerActivity;
import com.anbillon.lunarlite.domain.interactor.AlmanacUsecase;
import com.anbillon.lunarlite.ui.RxPresenter;
import com.coolerfall.widget.lunar.Lunar;

import javax.inject.Inject;

/**
 * A presenter controls communication between {@link LunarLiteView} and models of the presentation.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@PerActivity
public class LunarLitePresenter extends RxPresenter<LunarLiteView> {
	private AlmanacUsecase almanacUsecase;

	@Inject public LunarLitePresenter(AlmanacUsecase almanacUsecase) {
		this.almanacUsecase = almanacUsecase;
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
	 * @param wielding wielding of god
	 * @param heavenlyEarthly heavenly stems and earthly branches
	 */
	protected void loadAlmanac(int wielding, int heavenlyEarthly) {
		subscriptions.add(almanacUsecase.getAuspiciousDay(wielding, heavenlyEarthly)
			.filter(almanac -> almanac != null).subscribe(view::bindAlmanac));
	}
}
