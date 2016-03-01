package com.coolerfall.lunarlite.ui.lunar;

import android.content.Context;

import com.coolerfall.lunarlite.R;
import com.coolerfall.lunarlite.di.scope.PerActivity;
import com.coolerfall.lunarlite.domain.interactor.AlmanacUsecase;
import com.coolerfall.lunarlite.ui.Presenter;
import com.coolerfall.lunarlite.ui.View;
import com.coolerfall.lunarlite.utils.Misc;
import com.coolerfall.widget.lunar.Lunar;

import java.io.File;

import javax.inject.Inject;

import rx.Subscription;

/**
 * A presenter controls communication between {@link LunarLiteView} and models of the presentation.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @since Feb. 29, 2016
 */
@PerActivity
public class LunarLitePresenter implements Presenter {
	private static final String DB_NAME = "almanac.db";
	private Context mContext;
	private AlmanacUsecase mAlmanacUsecase;
	private Subscription mAlmanacSubscription;
	private LunarLiteView mView;

	@Inject
	public LunarLitePresenter(Context context, AlmanacUsecase almanacUsecase) {
		mContext = context;
		mAlmanacUsecase = almanacUsecase;
	}

	@Override
	public void attach(View view) {
		mView = (LunarLiteView) view;
		copyDatabase();
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

	private void copyDatabase() {
		File dbFile = mContext.getDatabasePath(DB_NAME);
		if (!dbFile.exists()) {
			Misc.copyRawDatabase(mContext, R.raw.almanac, DB_NAME);
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
			.subscribe(mView::bindAlmanac);
	}
}
