package com.anbillon.lunarlite.domain.repository;

import com.anbillon.lunarlite.data.db.model.Almanac;

import rx.Observable;

/**
 * Interface that represents a repository for getting related data from database.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
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
