package com.coolerfall.lunarlite.domain.repository;

import com.coolerfall.lunarlite.data.db.model.Almanac;

import rx.Observable;

/**
 * Interface that represents a repository for getting related data from database.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @since Feb. 20, 2016
 */
public interface AlmanacRepository {
	/**
	 * Get an almanac from database with given wielding and heavenly earthly.
	 *
	 * @param wielding        wielding
	 * @param heavenlyEarthly heavenly and earthly
	 * @return {@link Almanac}
	 */
	Observable<Almanac> getAlmanac(int wielding, int heavenlyEarthly);
}
