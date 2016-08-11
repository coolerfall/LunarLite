package com.anbillon.lunarlite.data.db.datasource;

import com.anbillon.lunarlite.data.db.model.Almanac;
import com.anbillon.lunarlite.domain.repository.AlmanacRepository;
import com.squareup.sqlbrite.BriteDatabase;
import javax.inject.Inject;
import rx.Observable;

/**
 * Provide almanac data source from database.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class AlmanacDataSource implements AlmanacRepository {
	private final BriteDatabase briteDatabase;

	@Inject public AlmanacDataSource(BriteDatabase briteDatabase) {
		this.briteDatabase = briteDatabase;
	}

	@Override public Observable<Almanac> getAlmanac(int wielding, int heavenlyEarthly) {
		return briteDatabase.createQuery(Almanac.TABLE_NAME, Almanac.SELECT_BY_WIELDING_AND_HSEB,
			Integer.toString(wielding), Integer.toString(heavenlyEarthly))
			.mapToOne(Almanac.MAPPER::map)
			.limit(1);
	}
}
