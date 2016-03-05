package com.coolerfall.lunarlite.domain.interactor;

import com.coolerfall.lunarlite.data.db.model.Almanac;
import com.coolerfall.lunarlite.domain.repository.AlmanacRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This class represents a almanac use case for retrieving data from database.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @since Feb. 20, 2016
 */
public class AlmanacUsecase {
	private AlmanacRepository mRepository;

	@Inject
	public AlmanacUsecase(AlmanacRepository repository) {
		mRepository = repository;
	}

	/**
	 * Get an auspicious day from database and observe on {@link AndroidSchedulers#mainThread()}.
	 *
	 * @param wielding        wielding
	 * @param heavenlyEarthly heavenly and earthly
	 * @return {@link Almanac} observable
	 */
	public Observable<Almanac> getAuspiciousDay(int wielding, int heavenlyEarthly) {
		return mRepository.getAlmanac(wielding, heavenlyEarthly)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread());
	}
}
